package models

import _root_.utils.TimeProvider
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

case class TVProgram(channel: String, start: DateTime, end: DateTime, category: Option[List[String]],
                     accessibility: Option[List[String]], series: Option[Serie], program: Option[Program], id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class TVProgramShort(channel: String, startTime: DateTime, endTime: DateTime, category: Option[List[String]], series: Option[SerieShort], program: Option[ProgramShort], id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {
  val uriTVProgramDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}


object TVShort {
  def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel, tvProgram.start, tvProgram.end, tvProgram.category, tvProgram.series.map(s => SerieShort(s.serieTitle)),
    tvProgram.program.map(p => ProgramShort(p.title)), tvProgram.id)
}

case class Serie(serieTitle: String, episodeTitle: String, description: Option[String], seasonNumber: Option[String], episodeNumber: Option[String], totalNumber: Option[String])

case class Program(title: String, description: Option[String])

case class SerieShort(serieTitle: String)

case class ProgramShort(title: String)


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

  implicit val programFmt = Json.format[Program]
  implicit val serieFmt = Json.format[Serie]
  val pattern = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val tvProgramFmt = Json.format[TVProgram]
  implicit val programShortFmt = Json.format[ProgramShort]
  implicit val serieShortFmt = Json.format[SerieShort]

  implicit val tvProgramShortReads: Reads[TVProgramShort] = (
    (__ \ "channel").read[String] and
      (__ \ "start").read[DateTime] and
      (__ \ "end").read[DateTime] and
      (__ \ "category").read[Option[List[String]]] and
      (__ \ "series").read[Option[SerieShort]] and
      (__ \ "program").read[Option[ProgramShort]] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVProgramShort.apply _)


  implicit val tvProgramShortWrites = new Writes[TVProgramShort] {
    override def writes(tvprogram: TVProgramShort): JsValue = Json.obj(
      "channel" -> tvprogram.channel,
      "start" -> tvprogram.startTime,
      "end" -> tvprogram.endTime,
      "category" -> tvprogram.category,
      "series" -> tvprogram.series,
      "program" -> tvprogram.program,
      "uriTVProgramDetails" -> tvprogram.uriTVProgramDetails,
      "id" -> tvprogram.id
    )
  }

  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value),
        Option(doc.getAs[List[String]]("category").toList.flatten),
        Option(doc.getAs[List[String]]("accessibility").toList.flatten),
        doc.getAs[BSONDocument]("serie").map(SerieBSONReader.read(_)),
        doc.getAs[BSONDocument]("program").map(ProgramBSONReader.read(_)),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }

  implicit object TVProgramShortContentBSONReader extends BSONDocumentReader[TVProgramShort] {
    def read(doc: BSONDocument): TVProgramShort = {
      TVProgramShort(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value),
        Option(doc.getAs[List[String]]("category").toList.flatten),
        doc.getAs[BSONDocument]("serie").map(SerieShortBSONReader.read(_)),
        doc.getAs[BSONDocument]("program").map(ProgramShortBSONReader.read(_)),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }

  implicit object TVProgramShortContentBSONWriter extends BSONDocumentWriter[TVProgramShort] {
    override def write(t: TVProgramShort): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channel" -> t.channel,
        "startTime" -> new BSONDateTime(t.startTime.getMillis),
        "endTime" -> new BSONDateTime(t.endTime.getMillis),
        "category" -> t.category
      )
    }
  }

  implicit object TVProgramContentBSONWriter extends BSONDocumentWriter[TVProgram] {
    override def write(t: TVProgram): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channel" -> t.channel,
        "startTime" -> new BSONDateTime(t.start.getMillis),
        "endTime" -> new BSONDateTime(t.end.getMillis),
        "category" -> t.category,
        "accessibility" -> t.accessibility,
        "serie" -> t.series.map(SerieBSONWriter.write(_)),
        "program" -> t.program.map(ProgramBSONWriter.write(_))
      )
    }
  }


  implicit object SerieBSONReader extends BSONDocumentReader[Serie] {
    def read(doc: BSONDocument): Serie = {
      Serie(
        doc.getAs[BSONString]("serieTitle").get.value,
        doc.getAs[BSONString]("episodeTitle").get.value,
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
        "episodeTitle" -> t.episodeTitle,
        "description" -> t.description,
        "seasonNumber" -> t.seasonNumber,
        "episodeNumber" -> t.episodeNumber,
        "totalNumber" -> t.totalNumber
      )
    }
  }

  implicit object SerieShortBSONReader extends BSONDocumentReader[SerieShort] {
    def read(doc: BSONDocument): SerieShort = {
      SerieShort(
        doc.getAs[BSONString]("serieTitle").get.value
      )
    }
  }


  implicit object SerieShortBSONWriter extends BSONDocumentWriter[SerieShort] {
    override def write(t: SerieShort): BSONDocument = {
      BSONDocument(
        "serieTitle" -> t.serieTitle
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

  implicit object ProgramShortBSONReader extends BSONDocumentReader[ProgramShort] {
    def read(doc: BSONDocument): ProgramShort = {
      ProgramShort(
        doc.getAs[BSONString]("title").get.value
      )
    }
  }


  implicit object ProgramShortBSONWriter extends BSONDocumentWriter[ProgramShort] {
    override def write(t: ProgramShort): BSONDocument = {
      BSONDocument(
        "title" -> t.title
      )
    }
  }


}

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = ???

  def findContentByID(contentID: String): Future[Option[TVProgram]] = ???

}

class TVContentRepository(name: String) extends ContentRepository with Connection with TimeProvider {

  override lazy val collectionName = name

  override def findDayContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = {

    import models.TVProgram.TVProgramShortContentBSONReader

    val query = BSONDocument(
      "$orderby" -> BSONDocument("startTime" -> 1),
      "$query" -> BSONDocument("channel" -> channelName)
    )
    val found = collection.find(query).cursor[TVProgramShort]
    found.collect[Seq]()
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "startTime" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "endTime" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    collection.find(query).one[TVProgram]

  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = {
    import models.TVProgram.TVProgramShortContentBSONReader

    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("startTime" -> 1),
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "endTime" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVProgramShort]
    found.collect[Seq]()
  }

  override def findContentByID(contentID: String): Future[Option[TVProgram]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "_id" -> BSONObjectID(contentID)))

    collection.find(query).one[TVProgram]
  }
}

object TVContentRepository {
  def apply(collectionName: String) = new TVContentRepository(collectionName)
}



