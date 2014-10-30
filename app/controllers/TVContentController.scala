package controllers

import java.net.URLDecoder

import models._
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, SimpleResult}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global

object TVContentController extends TVContentController {
  override val contentRepository = new TVContentRepository("tvContent")
}

trait TVContentController extends BaseController {

  val contentRepository: ContentRepository

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      buildResponseForIndividualContent(_, s"No TV content at this moment for the channel: $channelName")
    }
  }

  def tvContentDetails(tvContentID: String) = Action.async {
    contentRepository.findContentByID(tvContentID).map {
      buildResponseForIndividualContent(_, s"No TV content details with id: $tvContentID")
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      buildResponseForListOfContent(_, s"No TV content left for the channel: $channelName")
    }
  }

  def allContent(channelName: String) = Action.async {

    contentRepository.findDayContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      buildResponseForListOfContent(_, s"No TV content for the channel: $channelName")
    }
  }

  def contentByType(contentType: String) = Action.async {
    contentRepository.findDayContentByType(contentType.toLowerCase).map {
      buildResponseForListOfContent(_, s"No TV content for the type: $contentType")
    }
  }

  def currentContentByType(contentType: String) = Action.async {
    contentRepository.findCurrentContentByType(contentType.toLowerCase()).map {
      buildResponseForListOfContent(_, s"No TV content at this moment for the type: $contentType")
    }
  }

  def contentLeftByType(contentType: String) = Action.async {
    contentRepository.findLeftContentByType(contentType.toLowerCase()).map {
      buildResponseForListOfContent(_, s"No TV content left for the type: $contentType")
    }
  }

  private def buildResponseForListOfContent(content: Seq[TVContent], message: String): SimpleResult = {
    content match {
      case content if content.size > 0 => Ok(Json.toJson(content.map(TVShort(_))))
      case _ => NotFound(Json.toJson(NotFoundResponse(message)))
    }
  }

  private def buildResponseForIndividualContent(content: Option[TVContent], message: String): SimpleResult = {
    content match {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound(Json.toJson(NotFoundResponse(message)))
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
