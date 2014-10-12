package controllers

import models.{ChannelRepository, TVChannelRepository}
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelController extends TVChannelController {

  override val channelRepository = new TVChannelRepository("tvChannel")
}
trait TVChannelController extends BaseController {

  val channelRepository : ChannelRepository

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def channelsByCategory(category: String) = Action.async {
    channelRepository.listOfTVChannelsByCategory(category.toUpperCase).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }
}
