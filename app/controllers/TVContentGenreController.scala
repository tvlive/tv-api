package controllers

import models.{ContentGenreReporitory, TVContentGenreRepository}
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

object TVContentGenreController extends TVContentGenreController {
  override val contentGenreReporitory: ContentGenreReporitory = new TVContentGenreRepository("tvChannelGenre")
}

trait TVContentGenreController extends BaseController {
  val contentGenreReporitory: ContentGenreReporitory


  def genres() = Action.async {
    contentGenreReporitory.findAll().map{
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => {
        NotFound
      }
    }
  }
}
