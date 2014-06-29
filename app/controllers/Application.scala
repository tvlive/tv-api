package controllers

import play.api.libs.json.{Json, Writes, JsPath}
import play.api.mvc._
import uk.freview.api.model.{TVChannel, FakeRepositoy, TVRepository}
import scala.concurrent.Future


//object Application {
//
//  def apply() = new Application()
//}

//class Application(tvRepository: TVRepository) extends Controller {



object Application extends Controller {

  val tvRepository = new FakeRepositoy()

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    Future.successful {
      Ok(Json.toJson(tvRepository.listOfTVChannels()))
    }
  }
}