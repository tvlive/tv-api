package controllers

import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Controller, SimpleResult}

case class NotFoundResponse(reason: String, status: Int = 404)

case class BadRequestResponse(reason: String, status: Int = 400)

case class InternalErrorServerResponse(reason: String, status: Int = 500)

trait BaseController extends Controller {

  def buildResponseSeq[T](content: Seq[T])(implicit w: Writes[T]): SimpleResult = {
    Ok(Json.toJson(content))
  }

  def buildResponse[T](content: Option[T], message: String)(implicit w: Writes[T]): SimpleResult = {
    content match {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound(Json.toJson(NotFoundResponse(message)))
    }
  }
}
