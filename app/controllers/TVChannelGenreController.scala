package controllers

import models.TVChannelGenreRepository
import play.api.mvc.Action

import scala.concurrent.Future

object TVChannelGenreController extends TVChannelGenreController {
  override val channelGenreReporitory: TVChannelGenreRepository = new TVChannelGenreRepository("tvChannelGenre")
}

trait TVChannelGenreController extends BaseController {
  val channelGenreReporitory: TVChannelGenreRepository = ???

  def genres() = Action.async {
    Future.successful(BadRequest(""))
  }

}
