package controllers

import configuration.ApplicationContext._
import models.ChannelCategoryRepository
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelCategoryController extends TVChannelCategoryController {
  override val channelCategoryReporitory = tvChannelCategoryRepository
}

trait TVChannelCategoryController extends BaseController {
  val channelCategoryReporitory: ChannelCategoryRepository

  def categories() = Action.async {
    channelCategoryReporitory.findAll().map {
      buildResponseSeq(_, "No channel categories found")
    }
  }
}
