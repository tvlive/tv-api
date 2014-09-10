package controllers

import models.TVChannelRepository
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TVChannelController extends TVChannelController {
  override val channelRepository = TVChannelRepository("tvChannel")
}
trait TVChannelController extends Controller {

  val channelRepository : TVChannelRepository

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def channelsByGenre(genre: String) = Action.async {
    Future.successful(BadRequest(""))
  }
}
