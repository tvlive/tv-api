package controllers

import models._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


//object Application extends Application {
//  override val channelRepository = TVChannelRepository("tvChannel")
//  override val contentRepository = TVContentRepository("tvContent")
//}
//

object Application extends Controller {

  val channelRepository = TVChannelRepository("tvChannel")
  val contentRepository = TVContentRepository("tvContent")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      channels => Ok(Json.toJson(channels))
    }
  }

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(channelName).map {
      tvProgram => Ok(Json.toJson(tvProgram))
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(channelName).map {
      tvPrograms => Ok(Json.toJson(tvPrograms))
    }
  }

  def allContent(channelName: String) = Action.async {
    contentRepository.findDayContentByChannel(channelName).map {
      tvPrograms => Ok(Json.toJson(tvPrograms))
    }
  }

}