package configuration

import controllers.{BadRequestResponse, InternalErrorServerResponse}
import infrastructure.{AuthFilter, AuditFilter}
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, SimpleResult, WithFilters}

import scala.concurrent.Future

object Global extends WithFilters(AuditFilter, AuthFilter) {

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = {
    Future.successful {
      InternalServerError(Json.toJson(InternalErrorServerResponse(ex.getMessage)))
    }
  }

  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = {
    onBadRequest(request, "Resource not found")
  }

  override def onBadRequest(request: RequestHeader, error: String): Future[SimpleResult] = {
    Future.successful {
      BadRequest(Json.toJson(BadRequestResponse(error)))
    }
  }


}
