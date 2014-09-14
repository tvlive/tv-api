import org.joda.time.{DateTimeZone, DateTime}
import reactivemongo.bson._

package object models {

  implicit object TVChannelBSONReader extends BSONDocumentReader[TVChannel] {
    def read(doc: BSONDocument): TVChannel = {
      TVChannel(
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("genre").get.value,
        doc.getAs[BSONString]("language").get.value,
        doc.getAs[BSONObjectID]("_id"))
    }
  }

  implicit object TVChannelBSONWriter extends BSONDocumentWriter[TVChannel] {
    override def write(t: TVChannel): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "name" -> t.name,
        "genre" -> t.genre,
        "language" -> t.language
      )
    }
  }


  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value, DateTimeZone.forID("Europe/London")),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value, DateTimeZone.forID("Europe/London")),
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
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value, DateTimeZone.forID("Europe/London")),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value, DateTimeZone.forID("Europe/London")),
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
