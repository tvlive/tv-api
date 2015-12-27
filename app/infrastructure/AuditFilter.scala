package infrastructure

import gateway.AuthGateway
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success}

object AuditFilter extends Filter {
  override def apply(nextFilter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map {
      result =>
        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime
        Logger.info(s"${requestHeader.method} ${requestHeader.uri} " +
          s"took ${requestTime}ms and returned ${result.header.status}")

        result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}


trait AuthFilter extends Filter with Results {
  val authGateway: AuthGateway
  val decoder: Decryptor

  override def apply(nextFilter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
    nextFilter(requestHeader).flatMap {
      result =>
        if (requestHeader.uri.contains("/ping"))
          Future.successful(result)
        else {
          (requestHeader.method, requestHeader.headers.get("Authorization")) match {
            case ("POST", _) => Future.successful(result)
            case ("GET", Some(authEncoded)) => decoder.decodeToken(authEncoded) match {
              case Success(a) => authGateway.findToken(a.username, a.token).map {
                case true => result
                case false =>
                  Logger.error(s"Credentials with username:${a.username} and token:${a.token} do no exist")
                  Unauthorized
              }
              case Failure(e) =>
                Logger.error(s"Credentials encoded:$authEncoded do no have the correct format")
                Future.successful(Unauthorized)
            }
            case (_, _) =>
              Logger.error(s"No Authorization header in request")
              Future.successful(Unauthorized)
          }
        }
    }
  }
}

object AuthFilter extends AuthFilter {
  override lazy val authGateway: AuthGateway = AuthGateway
  override lazy val decoder: Decryptor = Decryptor
}