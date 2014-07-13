package models

import java.util.Date

import models.TVProgram.TVProgramContentBSONReader
import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVChannelContent(id: Option[BSONObjectID], channelName: String, program: TVProgram)

case class TVProgram(programName: String, start: Long, end: Long, typeProgram: String)



object TVProgram {

  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("programName").get.value,
        doc.getAs[BSONLong]("start").get.value,
        doc.getAs[BSONLong]("end").get.value,
        doc.getAs[BSONString]("typeProgram").get.value
      )
    }
  }


  implicit object TVProgramContentBSONWriter extends BSONDocumentWriter[TVProgram] {
    override def write(t: TVProgram): BSONDocument = {
      BSONDocument(
        "programName" -> t.programName,
        "start" -> t.start,
        "end" -> t.end,
        "typeProgram" -> t.typeProgram
      )
    }
  }

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

  implicit object TVChannelContentBSONReader extends BSONDocumentReader[TVChannelContent] {
    def read(doc: BSONDocument): TVChannelContent = {
      TVChannelContent(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("channelName").get.value,
        TVProgramContentBSONReader.read(doc.getAs[BSONDocument]("program").get)
      )
    }
  }

  implicit object TVChannelContentBSONWriter extends BSONDocumentWriter[TVChannelContent] {
    override def write(t: TVChannelContent): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channelName" -> t.channelName,
        "program" -> t.program
      )
    }
  }



  implicit object TVContentFormat extends Format[TVChannelContent] {

    def writes(tvContent: TVChannelContent): JsValue = {
      val tvChannelContentSeq = Seq(
        "name" -> JsString(tvContent.channelName)
      )
      JsObject(tvChannelContentSeq)
    }

    def reads(json: JsValue): JsResult[TVChannelContent] = {
      JsSuccess(TVChannelContent(Some(BSONObjectID.generate), "", TVProgram("",0,0,"")))
    }
  }

}

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Seq[TVProgram] = ???

  def findDayContentByChannel(channelName: String): Future[Option[TVChannelContent]] = ???

  def findCurrentContentByChannel(channelName: String): TVProgram = ???

}

class TVChannelContentRepository(name: String) extends ContentRepository with Connection {

  override lazy val collectionName = name
  override def findDayContentByChannel(channelName: String): Future[Option[TVChannelContent]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument("channelName" -> channelName)
    )
    collection.find(query).one[TVChannelContent]
  }
}

object TVChannelContentRepository {
  def apply(collectionName: String) = new TVChannelContentRepository(collectionName)
}

class FakeContentRepository extends ContentRepository {

  override def findLeftContentByChannel(channelName: String): Seq[TVProgram] = {
    Seq(
      TVProgram("programName1", new Date(2014, 6, 10, 10, 0, 0).getTime, new Date(2014, 6, 10, 11, 0, 0).getTime, "film"),
      TVProgram("programName2", new Date(2014, 6, 10, 11, 0, 0).getTime, new Date(2014, 6, 10, 12, 0, 0).getTime, "film")
    )
  }

  override def findDayContentByChannel(channelName: String): Future[Option[TVChannelContent]] = {
    Future {
      Some(TVChannelContent(Some(BSONObjectID.generate), "channel1", TVProgram("", 0, 1, "")))
    }
  }

  override def findCurrentContentByChannel(channelName: String): TVProgram = {
    TVProgram("programName3", new Date(2014, 6, 10, 10, 0, 0).getTime, new Date(2014, 6, 10, 11, 0, 0).getTime, "film")
  }
}

