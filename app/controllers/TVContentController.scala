package controllers

import java.net.URLDecoder

import models.{ContentRepository, TVContentRepository}
import play.api.libs.json.Json
import play.api.mvc.Action

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
      case head :: tail => Ok(Json.toJson(head :: tail))
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content left for the channel: $channelName")))
    }
  }

  def allContent(channelName: String) = Action.async {

    contentRepository.findDayContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
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
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content for the type: $contentType")))
    }
  }

  //
  def currentContentByType(contentType: String) = Action.async {
    contentRepository.findCurrentContentByType(contentType.toLowerCase()).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content at this moment for the type: $contentType")))
    }
  }

  def contentLeftByType(contentType: String) = Action.async {
    contentRepository.findLeftContentByType(contentType.toLowerCase()).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound(Json.toJson(NotFoundResponse(s"No TV content left for the type: $contentType")))
    }
  }
}
