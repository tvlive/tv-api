package controllers

import models.{ChannelRepository, TVChannel, TVChannelRepository}
import play.api.libs.json.Json
import play.api.mvc.{Action, SimpleResult}

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelController extends TVChannelController {

  override val channelRepository = new TVChannelRepository("tvChannel")
}
trait TVChannelController extends BaseController {

  val channelRepository: ChannelRepository

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      buildResponseForListOfChannels(_, "No channels found")
    }
  }

  def channelsByCategory(category: String) = Action.async {
    channelRepository.listOfTVChannelsByCategory(category.toUpperCase).map {
      buildResponseForListOfChannels(_, s"No channels found for the category: $category")
    }
  }

  def channelsByProvider(provider: String) = Action.async {
    channelRepository.listOfTVChannelsByProvider(provider.toUpperCase).map {
      buildResponseForListOfChannels(_, s"No channels found for the provider: $provider")
    }
  }

  private def buildResponseForListOfChannels(channels: Seq[TVChannel], message: String): SimpleResult = {
    channels match {
      case channels if channels.size > 0 => Ok(Json.toJson(channels))
      case _ => NotFound(Json.toJson(NotFoundResponse(message)))
    }
  }
}
