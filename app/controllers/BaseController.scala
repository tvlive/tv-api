package controllers

import models.APIMongoConnection
import play.api.mvc.Controller

trait BaseController extends Controller {

  implicit val conn : String => APIMongoConnection = {
    cn => new APIMongoConnection {
      override lazy val collectionName = cn
    }
  }


}
