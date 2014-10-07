import org.joda.time.{DateTime, DateTimeZone}
import reactivemongo.bson._

package object models {

  implicit object TVChannelBSONReader extends BSONDocumentReader[TVChannel] {
    def read(doc: BSONDocument): TVChannel = {
      TVChannel(
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[List[String]]("provider").toList.flatten,
        doc.getAs[BSONObjectID]("_id"))
    }
  }

  implicit object TVChannelBSONWriter extends BSONDocumentWriter[TVChannel] {
    override def write(t: TVChannel): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "name" -> t.name,
        "provider" -> t.provider
      )
    }
  }


  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value, DateTimeZone.forID("UTC")),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value, DateTimeZone.forID("UTC")),
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
        new DateTime(doc.getAs[BSONDateTime]("startTime").get.value, DateTimeZone.forID("UTC")),
        new DateTime(doc.getAs[BSONDateTime]("endTime").get.value, DateTimeZone.forID("UTC")),
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
        "film" -> t.film.map(ProgramBSONWriter.write(_))
      )
    }
  }


  implicit object SerieBSONReader extends BSONDocumentReader[Series] {
    def read(doc: BSONDocument): Series = {
      Series(
        doc.getAs[BSONString]("serieTitle").get.value,
        doc.getAs[BSONString]("episodeTitle").get.value,
        doc.getAs[BSONString]("description").map(_.value),
        doc.getAs[BSONString]("seasonNumber").map(_.value),
        doc.getAs[BSONString]("episodeNumber").map(_.value),
        doc.getAs[BSONString]("totalNumber").map(_.value)
      )
    }
  }


  implicit object SerieBSONWriter extends BSONDocumentWriter[Series] {
    override def write(t: Series): BSONDocument = {
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

  implicit object SerieShortBSONReader extends BSONDocumentReader[SeriesShort] {
    def read(doc: BSONDocument): SeriesShort = {
      SeriesShort(
        doc.getAs[BSONString]("serieTitle").get.value
      )
    }
  }


  implicit object SerieShortBSONWriter extends BSONDocumentWriter[SeriesShort] {
    override def write(t: SeriesShort): BSONDocument = {
      BSONDocument(
        "serieTitle" -> t.serieTitle
      )
    }
  }

  implicit object ProgramBSONReader extends BSONDocumentReader[Film] {
    def read(doc: BSONDocument): Film = {
      Film(
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONString]("description").map(_.value)
      )
    }
  }


  implicit object ProgramBSONWriter extends BSONDocumentWriter[Film] {
    override def write(t: Film): BSONDocument = {
      BSONDocument(
        "title" -> t.title,
        "description" -> t.description
      )
    }
  }

  implicit object ProgramShortBSONReader extends BSONDocumentReader[FilmShort] {
    def read(doc: BSONDocument): FilmShort = {
      FilmShort(
        doc.getAs[BSONString]("title").get.value
      )
    }
  }


  implicit object ProgramShortBSONWriter extends BSONDocumentWriter[FilmShort] {
    override def write(t: FilmShort): BSONDocument = {
      BSONDocument(
        "title" -> t.title
      )
    }
  }

  implicit object TVChannelGenreBSONReader extends BSONDocumentReader[TVChannelGenre] {
    def read(doc: BSONDocument): TVChannelGenre = {
      TVChannelGenre(
        doc.getAs[BSONString]("genre").get.value,
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVChannelGenreBSONWriter extends BSONDocumentWriter[TVChannelGenre] {
    override def write(t: TVChannelGenre): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "genre" -> t.genre
      )
    }
  }

  implicit object TVContentGenreBSONReader extends BSONDocumentReader[TVContentGenre] {
    def read(doc: BSONDocument): TVContentGenre = {
      TVContentGenre(
        doc.getAs[BSONString]("genre").get.value,
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVContentGenreBSONWriter extends BSONDocumentWriter[TVContentGenre] {
    override def write(t: TVContentGenre): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "genre" -> t.genre
      )
    }
  }
}
