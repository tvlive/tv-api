package controllers

import play.api.libs.json.{Json, Writes, JsPath}
import play.api.mvc._
import uk.freview.api.model.{FakeContentRepository, TVChannel, FakeChannelRepositoy, ChannelRepository}
import scala.concurrent.Future


//object Application {
//
//  def apply() = new Application()
//}

//class Application(tvRepository: TVRepository) extends Controller {



object Application extends Controller {

  val tvChannelRepository = new FakeChannelRepositoy()
  
  val tvContentRepository = new FakeContentRepository()

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    Future.successful {
      Ok(Json.toJson(tvChannelRepository.listOfTVChannels()))
    }
  }

  def currentContent(channelName: String) = Action.async {
    Future.successful {
      Ok(Json.toJson(tvContentRepository.findCurrentContentByChannel(channelName)))
    }
  }

  def contentLeft(channelName: String) = Action.async {
    Future.successful {
      Ok(Json.toJson(tvContentRepository.findLeftContentByChannel(channelName)))
    }
  }

  def allContent(channelName: String) = Action.async {
    Future.successful {
      Ok(Json.toJson(tvContentRepository.findDayContentByChannel(channelName)))
    }
  }
}