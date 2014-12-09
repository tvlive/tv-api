package controllers

import configuration.ApplicationContext._
import models.ChannelProviderRepository
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelProviderController extends TVChannelProviderController {
  override val channelProviderReporitory = tvChannelProviderReporitory
}

trait TVChannelProviderController extends BaseController {
  val channelProviderReporitory: ChannelProviderRepository

  def providers() = Action.async {
    channelProviderReporitory.findAll().map{
      buildResponseSeq(_, "No channel providers found")
    }
  }
}