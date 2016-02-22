package controllers

import configuration.ApplicationContext._
import controllers.external.{ChannelProviderExternal, TVChannelProviderExternal, ChannelCategoryExternal, TVChannelCategoryExternal}
import models.{ChannelProviderRepository, TVChannelProvider}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelProviderController extends TVChannelProviderController {
  override val channelProviderReporitory = tvChannelProviderReporitory
}

trait TVChannelProviderController extends BaseController {
  val channelProviderReporitory: ChannelProviderRepository
  val toProviders: Seq[TVChannelProvider] => Seq[TVChannelProviderExternal] = _.map(ChannelProviderExternal(_))

  def providers() = Action.async {
    channelProviderReporitory.findAll().map{
      p => buildResponseSeq(toProviders(p))
    }
  }
}