package controllers

import models.APIMongoConnection
import play.api.mvc.Controller

case class NotFoundResponse(reason: String, status: Int = 404)

case class BadRequestResponse(reason: String, status: Int = 400)

case class InternalErrorServerResponse(reason: String, status: Int = 500)

trait BaseController extends Controller {

  implicit val conn : String => APIMongoConnection = {
    cn => new APIMongoConnection {
      override lazy val collectionName = cn
    }
  }


}
