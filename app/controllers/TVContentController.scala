package controllers

import java.net.URLDecoder

import models.TVContentRepository
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global


object TVContentController extends TVContentController {
  override val contentRepository = new TVContentRepository("tvContent")
}

trait TVContentController extends BaseController {

  val contentRepository: TVContentRepository

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case Some(tvProgram) => Ok(Json.toJson(tvProgram))
      case None => NotFound
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case head :: tail => Ok(Json.toJson(head :: tail))
      case Nil => NotFound
    }
  }

  def allContent(channelName: String) = Action.async {

    contentRepository.findDayContentByChannel(URLDecoder.decode(channelName, "UTF-8").toUpperCase).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def tvContentDetails(tvContentID: String) = Action.async {
    contentRepository.findContentByID(tvContentID).map {
      case Some(tvProgram) => Ok(Json.toJson(tvProgram))
      case None => NotFound
    }
  }

  def contentByGenre(genre: String) = Action.async {
    contentRepository.findDayContentByGenre(genre.toUpperCase()).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def currentContentByGenre(genre: String) = Action.async {
    contentRepository.findCurrentContentByGenre(genre.toUpperCase()).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }

  def contentLeftByGenre(genre: String) = Action.async {
    contentRepository.findLeftContentByGenre(genre.toUpperCase()).map {
      case head :: tail => {
        Ok(Json.toJson(head :: tail))
      }
      case Nil => NotFound
    }
  }
}
