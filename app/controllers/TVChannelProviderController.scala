package controllers

import models.{ChannelProviderRepository, TVChannelProviderRepository}
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelProviderController extends TVChannelProviderController {
  override val channelProviderReporitory = new TVChannelProviderRepository("tvChannelProvider")
}

trait TVChannelProviderController extends BaseController {
  val channelProviderReporitory: ChannelProviderRepository

  def providers() = Action.async {
    channelProviderReporitory.findAll().map{
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => {
        NotFound
      }
    }
  }
}