package models

import _root_.utils.TimeProvider
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

case class TVProgram(channel: String, startTime: DateTime, endTime: DateTime, category: Option[String],
                     flags: Option[String], serie: Option[Serie], program: Option[Program], id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class TVProgramShort(channel: String, startTime: DateTime, endTime: DateTime, category: Option[String], series: Option[SerieShort], program: Option[ProgramShort], id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {
  val uriTVProgramDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}


object TVShort {
  def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel, tvProgram.startTime, tvProgram.endTime, tvProgram.category, tvProgram.serie.map(s => SerieShort(s.serieTitle)),
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
      (__ \ "episodeTitle").read[String] and
      (__ \ "description").read[Option[String]] and
      (__ \ "seasonNumber").read[Option[String]] and
      (__ \ "episodeNumber").read[Option[String]] and
      (__ \ "totalNumber").read[Option[String]]
    )(Serie.apply _)


  implicit val serieWrites: Writes[Serie] = (
    (__ \ "serieTitle").write[String] and
      (__ \ "episodeTitle").write[String] and
      (__ \ "description").write[Option[String]] and
      (__ \ "seasonNumber").write[Option[String]] and
      (__ \ "episodeNumber").write[Option[String]] and
      (__ \ "totalNumber").write[Option[String]]
    )(unlift(Serie.unapply))

  val pattern = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val tvProgramReads: Reads[TVProgram] = (
    (__ \ "channel").read[String] and
      (__ \ "start").read[DateTime] and
      (__ \ "end").read[DateTime] and
      (__ \ "category").read[Option[String]] and
      (__ \ "flags").read[Option[String]] and
      (__ \ "series").read[Option[Serie]] and
      (__ \ "program").read[Option[Program]] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVProgram.apply _)


  implicit val tvProgramWrites = new Writes[TVProgram] {
    override def writes(tvprogram: TVProgram): JsValue = Json.obj(
      "channel" -> tvprogram.channel,
      "start" -> tvprogram.startTime,
      "end" -> tvprogram.endTime,
      "category" -> tvprogram.category,
      "flags" -> tvprogram.flags,
      "series" -> tvprogram.serie,
      "program" -> tvprogram.program,
      "id" -> tvprogram.id
    )
  }

  implicit val programShortReads = Json.reads[ProgramShort]
  implicit val seriesShortReads = Json.reads[SerieShort]

  implicit val programShortWrites = Json.writes[ProgramShort]
  implicit val seriesShortWrites = Json.writes[SerieShort]

  implicit val tvProgramShortReads: Reads[TVProgramShort] = (
    (__ \ "channel").read[String] and
      (__ \ "start").read[DateTime] and
      (__ \ "end").read[DateTime] and
      (__ \ "category").read[Option[String]] and
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
        doc.getAs[BSONString]("category").map(_.value),
        doc.getAs[BSONString]("flags").map(_.value),
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
        doc.getAs[BSONString]("category").map(_.value),
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
        "startTime" -> new BSONDateTime(t.startTime.getMillis),
        "endTime" -> new BSONDateTime(t.endTime.getMillis),
        "category" -> t.category,
        "flags" -> t.flags,
        "serie" -> t.serie.map(SerieBSONWriter.write(_)),
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

    import TVProgram.TVProgramShortContentBSONReader

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
    import TVProgram.TVProgramShortContentBSONReader

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



