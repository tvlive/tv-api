package controllers

import configuration.ApplicationContext._
import controllers.external.{ChannelCategoryExternal, TVChannelCategoryExternal, TVLong, TVContentLong}
import models.{TVChannelCategory, TVContent, ChannelCategoryRepository}
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVChannelCategoryController extends TVChannelCategoryController {
  override val channelCategoryReporitory = tvChannelCategoryRepository
}

trait TVChannelCategoryController extends BaseController {
  val channelCategoryReporitory: ChannelCategoryRepository
  val toCategories: Seq[TVChannelCategory] => Seq[TVChannelCategoryExternal] = _.map(ChannelCategoryExternal(_))

  def categories() = Action.async {
    channelCategoryReporitory.findAll().map { c => buildResponseSeq(toCategories(c)) }
  }
}
