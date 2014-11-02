package controllers

import models.{ChannelCategoryRepository, TVChannelCategoryRepository}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelCategoryController extends TVChannelCategoryController {
  override val channelCategoryReporitory = new TVChannelCategoryRepository("tvChannelCategory")
}

trait TVChannelCategoryController extends BaseController {
  val channelCategoryReporitory: ChannelCategoryRepository

  def categories() = Action.async {
    channelCategoryReporitory.findAll().map {
      buildResponseSeq(_, "No channel categories found")
    }
  }
}
