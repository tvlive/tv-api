package controllers

import configuration.ApplicationContext._
import models.ChannelRepository
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelController extends TVChannelController {
  override val channelRepository = tvChannelRepository
}

trait TVChannelController extends BaseController {

  val channelRepository: ChannelRepository

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      buildResponseSeq(_, "No channels found")
    }
  }

  def channelsByCategory(category: String) = Action.async {
    channelRepository.listOfTVChannelsByCategory(category.toUpperCase).map {
      buildResponseSeq(_, s"No channels found for the category: $category")
    }
  }

  def channelsByProvider(provider: String) = Action.async {
    channelRepository.listOfTVChannelsByProvider(provider.toUpperCase).map {
      buildResponseSeq(_, s"No channels found for the provider: $provider")
    }
  }

}
