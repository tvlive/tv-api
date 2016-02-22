package controllers

import java.net.URLDecoder

import configuration.{ApplicationContext, Environment}
import controllers.external.{TVContentLong, TVContentShort, TVLong, TVShort}
import models._
import play.api.mvc._
import utils.TimeProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TVContentController extends TVContentController {
  override val contentRepository = ApplicationContext.tvContentRepository
  override implicit val host: String = Environment.host
  override implicit val time: TimeProvider = ApplicationContext.time
}

trait TVContentController extends BaseController with Validation {

  implicit val host: String
  implicit val time: TimeProvider
  val toTVShorts: Seq[TVContent] => Seq[TVContentShort] = _.map(TVShort(_))
  val toTVLong: TVContent => TVContentLong = TVLong(_)

  val contentRepository: ContentRepository

  def currentContent(channelName: String) = Action.async { c =>
    contentRepository.findCurrentContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map { c =>
      buildResponse(c.map(toTVLong(_)), s"No TV content at this moment for the channel: $channelName")
    }
  }

  def tvContentDetails(tvContentID: String) = Action.async {
    contentRepository.findContentByID(tvContentID).map { c =>
      buildResponse(c.map(toTVLong(_)), s"No TV content details with id: $tvContentID")
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def allContent(channelName: String) = Action.async {
    contentRepository.findDayContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def allContentByTypeAndProvider(contentType: String, provider: String) = tvcontentValidation(contentType).async {
    cr: ContentRequest[_] => 
      contentRepository.findDayContentByTypeAndProvider(cr.content.toLowerCase, provider.toUpperCase).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def currentContentByTypeAndProvider(contentType: String, provider: String) = tvcontentValidation(contentType).async {
    cr: ContentRequest[_] =>
    contentRepository.findCurrentContentByTypeAndProvider(cr.content.toLowerCase, provider.toUpperCase).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def contentLeftByTypeAndProvider(contentType: String, provider: String) = tvcontentValidation(contentType).async {
    cr: ContentRequest[_] =>
    contentRepository.findLeftContentByTypeAndProvider(cr.content.toLowerCase(), provider.toUpperCase()).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def currentContentByProvider(provider: String) = Action.async {
    contentRepository.findCurrentContentByProvider(provider.toUpperCase()).map {
      ltv =>
        buildResponseSeq(toTVShorts(ltv))
    }
  }

  def topContentLeftByProvider(provider: String, items: Int = 10) = Action.async {
    contentRepository.findTopLeftContentByProvider(items, provider.toUpperCase()).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def contentNextByProvider(provider: String) = Action.async {
    contentRepository.findNextProgramByProvider(provider.toUpperCase()).map {
      ltv => buildResponseSeq(toTVShorts(ltv))
    }
  }

  def searchBy(provider: String, t: Option[String], c: Option[String], r: Option[Double]) = searchValidation(t, r, c).async {
    sr: SearchRequest[_] =>
      contentRepository.searchBy(provider.toUpperCase, sr.title, sr.content.map(_.toLowerCase), sr.rating).map {
        ltv => buildResponseSeq(toTVShorts(ltv))
      }
  }

  def contentNextByTypeAndProvider(contentType: String, provider: String) = tvcontentValidation(contentType).async {
    cr: ContentRequest[_] =>
      contentRepository.findNextContentByTypeAndProvider(cr.content.toLowerCase(), provider.toUpperCase()).map {
        ltv => buildResponseSeq(toTVShorts(ltv))
      }
  }

}
