package models

import _root_.utils.TimeProvider
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

case class TVProgram(channelName: String, programName: String, start: Long, end: Long, category: Option[String], flags: Option[String], serie: Serie, program: Program, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class Serie(serieTitle: String, description: Option[String], seasonNumber: Option[String], episodeNumber: Option[String], totalNumber: Option[String])

case class Program(title: String, description: Option[String])


object TVProgram {

  implicit object BSONObjectIDFormat extends Format[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = JsString(objectId.stringify)

    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(x) => {
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(x)
        if (maybeOID.isSuccess) JsSuccess(maybeOID.get)
        else {
          JsError("Expected BSONObjectID as JsString")
        }
      }
      case _ => JsError("Expected BSONObjectID as JsString")
    }
  }


  implicit val programReads: Reads[Program] = (
    (__ \ "title").read[String] and
      (__ \ "description").read[Option[String]]
    )(Program.apply _)

  implicit val programWrites: Writes[Program] = (
    (__ \ "title").write[String] and
      (__ \ "description").write[Option[String]]
    )(unlift(Program.unapply))

  implicit val serieReads: Reads[Serie] = (
    (__ \ "serieTitle").read[String] and
      (__ \ "description").read[Option[String]] and
      (__ \ "seasonNumber").read[Option[String]] and
      (__ \ "episodeNumber").read[Option[String]] and
      (__ \ "totalNumber").read[Option[String]]
    )(Serie.apply _)

  implicit val serieWrites: Writes[Serie] = (
    (__ \ "serieTitle").write[String] and
      (__ \ "description").write[Option[String]] and
      (__ \ "seasonNumber").write[Option[String]] and
      (__ \ "episodeNumber").write[Option[String]] and
      (__ \ "totalNumber").write[Option[String]]
    )(unlift(Serie.unapply))

  implicit val tvProgramReads: Reads[TVProgram] = (
    (__ \ "channelName").read[String] and
      (__ \ "programName").read[String] and
      (__ \ "start").read[Long] and
      (__ \ "end").read[Long] and
      (__ \ "category").read[Option[String]] and
      (__ \ "flags").read[Option[String]] and
      (__ \ "serie").read[Serie] and
      (__ \ "program").read[Program] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVProgram.apply _)

  implicit val tvProgramWrites: Writes[TVProgram] = (
    (__ \ "channelName").write[String] and
      (__ \ "programName").write[String] and
      (__ \ "start").write[Long] and
      (__ \ "end").write[Long] and
      (__ \ "category").write[Option[String]] and
      (__ \ "flags").write[Option[String]] and
      (__ \ "serie").write[Serie] and
      (__ \ "program").write[Program] and
      (__ \ "id").write[Option[BSONObjectID]]
    )(unlift(TVProgram.unapply))


  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("channelName").get.value,
        doc.getAs[BSONString]("programName").get.value,
        doc.getAs[BSONLong]("start").get.value,
        doc.getAs[BSONLong]("end").get.value,
        doc.getAs[BSONString]("category").map(_.value),
        doc.getAs[BSONString]("flags").map(_.value),
        SerieBSONReader.read(doc.getAs[BSONDocument]("serie").get),
        ProgramBSONReader.read(doc.getAs[BSONDocument]("program").get),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVProgramContentBSONWriter extends BSONDocumentWriter[TVProgram] {
    override def write(t: TVProgram): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channelName" -> t.channelName,
        "programName" -> t.programName,
        "start" -> t.start,
        "end" -> t.end,
        "category" -> t.category,
        "flags" -> t.flags,
        "serie" -> SerieBSONWriter.write(t.serie),
        "program" -> ProgramBSONWriter.write(t.program)
      )
    }
  }


  implicit object SerieBSONReader extends BSONDocumentReader[Serie] {
    def read(doc: BSONDocument): Serie = {
      Serie(
        doc.getAs[BSONString]("serieTitle").get.value,
        doc.getAs[BSONString]("description").map(_.value),
        doc.getAs[BSONString]("seasonNumber").map(_.value),
        doc.getAs[BSONString]("episodeNumber").map(_.value),
        doc.getAs[BSONString]("totalNumber").map(_.value)
      )
    }
  }


  implicit object SerieBSONWriter extends BSONDocumentWriter[Serie] {
    override def write(t: Serie): BSONDocument = {
      BSONDocument(
        "serieTitle" -> t.serieTitle,
        "description" -> t.description,
        "seasonNumber" -> t.seasonNumber,
        "episodeNumber" -> t.episodeNumber,
        "totalNumber" -> t.totalNumber
      )
    }
  }

  implicit object ProgramBSONReader extends BSONDocumentReader[Program] {
    def read(doc: BSONDocument): Program = {
      Program(
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONString]("description").map(_.value)
      )
    }
  }


  implicit object ProgramBSONWriter extends BSONDocumentWriter[Program] {
    override def write(t: Program): BSONDocument = {
      BSONDocument(
        "title" -> t.title,
        "description" -> t.description
      )
    }
  }

}

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVProgram]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVProgram]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = ???

}

class TVContentRepository(name: String) extends ContentRepository with Connection with TimeProvider {

  override lazy val collectionName = name

  override def findDayContentByChannel(channelName: String): Future[Seq[TVProgram]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument("channelName" -> channelName)
    )
    val found = collection.find(query).cursor[TVProgram]
    found.collect[Seq]()
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channelName" -> channelName,
        "start" -> BSONDocument("$lte" -> now),
        "end" -> BSONDocument("$gte" -> now)))

    collection.find(query).one[TVProgram]

  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVProgram]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument(
        "channelName" -> channelName,
        "end" -> BSONDocument("$gte" -> currentDate())))

    val found = collection.find(query).cursor[TVProgram]
    found.collect[Seq]()
  }
}

object TVContentRepository {
  def apply(collectionName: String) = new TVContentRepository(collectionName)
}



