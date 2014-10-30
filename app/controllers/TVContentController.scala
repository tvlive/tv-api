package controllers

import java.net.URLDecoder

import models._
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.Action
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global

object TVContentController extends TVContentController {
  override val contentRepository = new TVContentRepository("tvContent")
}

trait TVContentController extends BaseController {

  val contentRepository: ContentRepository

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case Some(tvProgram) => Ok(Json.toJson(tvProgram))
      case None => NotFound(Json.toJson(NotFoundResponse(s"No TV content at this moment for the channel: $channelName")))
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case head :: tail => Ok(Json.toJson(TVShort(head) :: tail.map(TVShort(_))))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content left for the channel: $channelName")))
    }
  }

  def allContent(channelName: String) = Action.async {

    contentRepository.findDayContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case head :: tail => Ok(Json.toJson(TVShort(head) :: tail.map(TVShort(_))))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content for the channel: $channelName")))
    }
  }

  def tvContentDetails(tvContentID: String) = Action.async {
    contentRepository.findContentByID(tvContentID).map {
      case Some(tvProgram) => Ok(Json.toJson(tvProgram))
      case None => NotFound(Json.toJson(NotFoundResponse(s"No TV content details with id: $tvContentID")))
    }
  }

  def contentByType(contentType: String) = Action.async {
    contentRepository.findDayContentByType(contentType.toLowerCase).map {
      case head :: tail => Ok(Json.toJson(TVShort(head) :: tail.map(TVShort(_))))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content for the type: $contentType")))
    }
  }

  //
  def currentContentByType(contentType: String) = Action.async {
    contentRepository.findCurrentContentByType(contentType.toLowerCase()).map {
      case head :: tail => Ok(Json.toJson(TVShort(head) :: tail.map(TVShort(_))))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content at this moment for the type: $contentType")))
    }
  }

  def contentLeftByType(contentType: String) = Action.async {
    contentRepository.findLeftContentByType(contentType.toLowerCase()).map {
      case head :: tail => Ok(Json.toJson(TVShort(head) :: tail.map(TVShort(_))))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content left for the type: $contentType")))
    }
  }
}

case class TVContentShort(channel: String,
                          start: DateTime,
                          end: DateTime,
                          category: Option[List[String]],
                          series: Option[SeriesShort],
                          film: Option[FilmShort],
                          program: Option[ProgramShort],
                          id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {

  val uriTVContentDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}

case class SeriesShort(serieTitle: String)

case class FilmShort(title: String)

case class ProgramShort(title: String)

object TVShort {
  def apply(tvProgram: TVContent): TVContentShort = TVContentShort(tvProgram.channel,
    tvProgram.start,
    tvProgram.end,
    tvProgram.category, tvProgram.series.map(s => SeriesShort(s.serieTitle)),
    tvProgram.film.map(f => FilmShort(f.title)),
    tvProgram.program.map(p => ProgramShort(p.title)),
    tvProgram.id)
}
