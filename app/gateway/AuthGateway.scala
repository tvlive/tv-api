package gateway

import configuration.Environment
import infrastructure.{BadRequestDownstreamException, InternalServerErrorDownstreamException}
import models.{Authorization, Token}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AuthGateway {
  val authUrl: String

  def createToken(authorization: Authorization): Future[Token] =
    WS.url(s"$authUrl/authorize")
      .withHeaders("Content-type" -> "application/json; charset=UTF-8")
      .post(Json.toJson(authorization)).map { res =>
      res.status match {
        case 201 => res.json.as[Token]
        case x if x >= 400 && x <= 499 =>
          Logger.error(s"Status code $x from downstream")
          throw BadRequestDownstreamException
        case x if x >= 500 && x <= 599 =>
          Logger.error(s"Status code $x from downstream")
          throw InternalServerErrorDownstreamException
      }
    }

  def findToken(username: String, token: String): Future[Boolean] = {
    WS.url(s"$authUrl/authorize/$username/$token")
      .withHeaders("Content-type" -> "application/json; charset=UTF-8")
      .get.map { res =>
      res.status match {
        case 200 => true
        case _ => false
      }
    }
  }
}

object AuthGateway extends AuthGateway {
  override val authUrl: String = Environment.auth
}


