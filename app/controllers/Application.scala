package controllers

import models.{FakeContentRepository, FakeTVChannelRepositoy}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future


//object Application {
//
//  def apply() = new Application()
//}

//class Application(tvRepository: TVRepository) extends Controller {



object Application extends Controller {

  val tvChannelRepository = FakeTVChannelRepositoy
  
  val tvContentRepository = new FakeContentRepository()

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    Future.successful {
      Ok("")//(Json.toJson(tvChannelRepository.listOfTVChannels()))
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
      Ok("")
//      Ok(Json.toJson(tvContentRepository.findDayContentByChannel(channelName)))
    }
  }
}