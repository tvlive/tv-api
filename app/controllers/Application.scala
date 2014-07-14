package controllers

import models._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


object Application extends Application {
  override val channelRepository = TVChannelRepository("tvChannel")
  override val contentRepository = TVContentRepository("tvContent")
}


trait Application extends Controller {

  implicit object TVProgramFormat extends Format[TVProgram] {


    def writes(tvProgram: TVProgram): JsValue = {
      val tvProgramSeq = Seq(
        "channelName" -> JsString(tvProgram.channelName),
        "programName" -> JsString(tvProgram.programName),
        "start" -> JsNumber(tvProgram.start),
        "end" -> JsNumber(tvProgram.end),
        "typeProgram" -> JsString(tvProgram.typeProgram)
      )
      JsObject(tvProgramSeq)
    }

    def reads(json: JsValue): JsResult[TVProgram] = {
      JsSuccess(TVProgram("", "", 0L, 0L, ""))
    }
  }

  implicit object TVChannelFormat extends Format[TVChannel] {

    def writes(tvChannel: TVChannel): JsValue = {
      val tvChannelSeq = Seq(
        "name" -> JsString(tvChannel.name),
        "language" -> JsString(tvChannel.language)
      )
      JsObject(tvChannelSeq)
    }

    def reads(json: JsValue): JsResult[TVChannel] = {
      JsSuccess(TVChannel("", ""))
    }
  }

  val channelRepository : ChannelRepository = ???
  val contentRepository : ContentRepository = ???

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def channels = Action.async {
    channelRepository.listOfTVChannels().map {
      channels => Ok(Json.toJson(channels))
    }
  }

  def currentContent(channelName: String) = Action.async {
    contentRepository.findCurrentContentByChannel(channelName).map {
      tvProgram => Ok(Json.toJson(tvProgram))
    }
  }

  def contentLeft(channelName: String) = Action.async {
    contentRepository.findLeftContentByChannel(channelName).map {
      tvPrograms => Ok(Json.toJson(tvPrograms))
    }
  }

  def allContent(channelName: String) = Action.async {
    contentRepository.findDayContentByChannel(channelName).map {
      tvPrograms => Ok(Json.toJson(tvPrograms))
    }
  }

}