package models

import java.util.Date

import play.api.libs.json._

case class TVChannelContent(channelName: String, program: Seq[TVProgram])

case class TVProgram(programName: String, start: Long, end: Long, typeProgram: String)


object TVProgram {

  implicit object TVProgramFormat extends Format[TVProgram] {

    def writes(tvProgram: TVProgram): JsValue = {
      val tvProgramSeq = Seq(
        "name" -> JsString(tvProgram.programName),
        "start" -> JsNumber(tvProgram.start),
        "end" -> JsNumber(tvProgram.end),
        "typeProgram" -> JsString(tvProgram.typeProgram)
      )
      JsObject(tvProgramSeq)
    }

    def reads(json: JsValue): JsResult[TVProgram] = {
      JsSuccess(TVProgram("", 0L, 0L, ""))
    }
  }

}

object TVChannelContent {

  implicit object TVContentFormat extends Format[TVChannelContent] {

    def writes(tvContent: TVChannelContent): JsValue = {
      val tvChannelContentSeq = Seq(
        "name" -> JsString(tvContent.channelName)
      )
      JsObject(tvChannelContentSeq)
    }

    def reads(json: JsValue): JsResult[TVChannelContent] = {
      JsSuccess(TVChannelContent("", Seq()))
    }
  }

}

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Seq[TVProgram] = ???

  def findDayContentByChannel(channelName: String): Seq[TVProgram] = ???

  def findCurrentContentByChannel(channelName: String): TVProgram = ???

}


//class TVContentRepository(name: String = "tvChannel")
//  extends SalatDAO[TVProgram, ObjectId](collection = MongoConnection()(mongodbDatabaseName)(name))
//  with ContentRepository {
//
//  override def findLeftContentByChannel(channelName: String): Seq[TVProgram] = Seq()
//
//  override def findDayContentByChannel(channelName: String): Seq[TVProgram] = Seq()
//
//  override def findCurrentContentByChannel(channelName: String): TVProgram = ???
//
//  //  def all(): List[TVChannelContent] = collection.find(MongoDBObject.empty).toList
//
//
//}


class FakeContentRepository extends ContentRepository {

  override def findLeftContentByChannel(channelName: String): Seq[TVProgram] = {
    Seq(
      TVProgram("programName1", new Date(2014, 6, 10, 10, 0, 0).getTime, new Date(2014, 6, 10, 11, 0, 0).getTime, "film"),
      TVProgram("programName2", new Date(2014, 6, 10, 11, 0, 0).getTime, new Date(2014, 6, 10, 12, 0, 0).getTime, "film")
    )
  }

  override def findDayContentByChannel(channelName: String): Seq[TVProgram] = {
    Seq(
      TVProgram("programName1", new Date(2014, 6, 10, 8, 0, 0).getTime, new Date(2014, 6, 10, 9, 0, 0).getTime, "film"),
      TVProgram("programName2", new Date(2014, 6, 10, 9, 0, 0).getTime, new Date(2014, 6, 10, 10, 0, 0).getTime, "film"),
      TVProgram("programName3", new Date(2014, 6, 10, 10, 0, 0).getTime, new Date(2014, 6, 10, 11, 0, 0).getTime, "film"),
      TVProgram("programName4", new Date(2014, 6, 10, 11, 0, 0).getTime, new Date(2014, 6, 10, 12, 0, 0).getTime, "film")
    )
  }

  override def findCurrentContentByChannel(channelName: String): TVProgram = {
    TVProgram("programName3", new Date(2014, 6, 10, 10, 0, 0).getTime, new Date(2014, 6, 10, 11, 0, 0).getTime, "film")
  }
}

