package gateway

import configuration.Environment
import infrastructure.{InternalServerErrorDownstreamException, BadRequestDownstreamException}
import models.{Authorization, Token}
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AuthGateway {
  val authUrl: String

  def createToken(authorization: Authorization): Future[Token] =
    WS.url(s"$authUrl/authorize").post(Json.toJson(authorization)).map { res =>
      res.status match {
        case 201 => res.json.as[Token]
        case x if x >= 400 && x <= 499 => throw BadRequestDownstreamException
        case x if x >= 500 && x <= 599 => throw InternalServerErrorDownstreamException
      }
    }
}

object AuthGateway extends AuthGateway {
  override val authUrl: String = Environment.auth
}


