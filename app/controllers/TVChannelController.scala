package controllers

import configuration.ApplicationContext._
import configuration.Environment
import controllers.external.{ChannelLong, TVChannelLong}
import models.{TVChannel, ChannelRepository}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelController extends TVChannelController {
  override val channelRepository = tvChannelRepository
  override implicit val host: String = Environment.host
}

trait TVChannelController extends BaseController {

  implicit val host: String
  val channelRepository: ChannelRepository
  val toTVChannel: Seq[TVChannel] => Seq[TVChannelLong] = _.map(ChannelLong(_))

  def channels = Action.async {
    channelRepository.listOfTVChannels().map { c => buildResponseSeq(toTVChannel(c)) }
  }

  def channelsByCategory(category: String) = Action.async {
    channelRepository.listOfTVChannelsByCategory(category.toUpperCase).map {
      c => buildResponseSeq(toTVChannel(c))
    }
  }

  def channelsByProvider(provider: String) = Action.async {
    channelRepository.listOfTVChannelsByProvider(provider.toUpperCase).map {
      c => buildResponseSeq(toTVChannel(c))
    }
  }

}
