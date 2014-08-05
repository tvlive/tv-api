package controllers

import models._
import play.api.libs.json._
import play.api.mvc._

import scala.collection.immutable.::
import scala.concurrent.ExecutionContext.Implicits.global


//object Application extends Application {
//  override val channelRepository = TVChannelRepository("tvChannel")
//  override val contentRepository = TVContentRepository("tvContent")
//}
//


//  todo add 404
object Application extends Controller {

  val channelRepository = TVChannelRepository("tvChannel")
  val contentRepository = TVContentRepository("tvContent")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(channelName).map {
      case Some(tvProgram) => Ok(Json.toJson(tvProgram))
      case None => NotFound
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(channelName).map {
      case head :: tail => Ok(Json.toJson(head :: tail))
      case Nil => NotFound
    }
  }

  def allContent(channelName: String) = Action.async {
    contentRepository.findDayContentByChannel(channelName).map {
      case head :: tail => Ok(Json.toJson(head :: tail))
      case Nil => NotFound
    }
  }

}