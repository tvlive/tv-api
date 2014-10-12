import org.joda.time.{DateTime, DateTimeZone}
import reactivemongo.bson._

package object models {

  implicit object TVChannelBSONReader extends BSONDocumentReader[TVChannel] {
    def read(doc: BSONDocument): TVChannel = {
      TVChannel(
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[List[String]]("provider").toList.flatten,
        doc.getAs[List[String]]("category").toList.flatten,
        doc.getAs[BSONObjectID]("_id"))
    }
  }

  implicit object TVChannelBSONWriter extends BSONDocumentWriter[TVChannel] {
    override def write(t: TVChannel): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "name" -> t.name,
        "provider" -> t.provider,
        "category" -> t.category
      )
    }
  }


  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVContent] {
    def read(doc: BSONDocument): TVContent = {
      TVContent(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("start").get.value, DateTimeZone.forID("UTC")),
        new DateTime(doc.getAs[BSONDateTime]("end").get.value, DateTimeZone.forID("UTC")),
        Option(doc.getAs[List[String]]("category").toList.flatten),
        doc.getAs[BSONDocument]("series").map(SerieBSONReader.read(_)),
        doc.getAs[BSONDocument]("film").map(FilmBSONReader.read(_)),
        doc.getAs[BSONDocument]("program").map(ProgramBSONReader.read(_)),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }

  implicit object TVProgramShortContentBSONReader extends BSONDocumentReader[TVContentShort] {
    def read(doc: BSONDocument): TVContentShort = {
      TVContentShort(
        doc.getAs[BSONString]("channel").get.value,
        new DateTime(doc.getAs[BSONDateTime]("start").get.value, DateTimeZone.forID("UTC")),
        new DateTime(doc.getAs[BSONDateTime]("end").get.value, DateTimeZone.forID("UTC")),
        Option(doc.getAs[List[String]]("category").toList.flatten),
        doc.getAs[BSONDocument]("series").map(SerieShortBSONReader.read(_)),
        doc.getAs[BSONDocument]("film").map(FilmShortBSONReader.read(_)),
        doc.getAs[BSONDocument]("program").map(ProgramShortBSONReader.read(_)),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }

  implicit object TVContentShortContentBSONWriter extends BSONDocumentWriter[TVContentShort] {
    override def write(t: TVContentShort): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channel" -> t.channel,
        "start" -> new BSONDateTime(t.start.getMillis),
        "end" -> new BSONDateTime(t.end.getMillis),
        "category" -> t.category
      )
    }
  }

  implicit object TVContentContentBSONWriter extends BSONDocumentWriter[TVContent] {
    override def write(t: TVContent): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channel" -> t.channel,
        "start" -> new BSONDateTime(t.start.getMillis),
        "end" -> new BSONDateTime(t.end.getMillis),
        "category" -> t.category,
        "series" -> t.series.map(SerieBSONWriter.write(_)),
        "film" -> t.film.map(FilmBSONWriter.write(_)),
        "program" -> t.program.map(ProgramBSONWriter.write(_))
      )
    }
  }


  implicit object SerieBSONReader extends BSONDocumentReader[Series] {
    def read(doc: BSONDocument): Series = {
      Series(
        doc.getAs[BSONString]("serieTitle").get.value,
        doc.getAs[BSONString]("episodeTitle").map(_.value),
        doc.getAs[BSONString]("description").map(_.value),
        doc.getAs[BSONString]("seasonNumber").map(_.value),
        doc.getAs[BSONString]("episodeNumber").map(_.value),
        doc.getAs[BSONString]("totalNumber").map(_.value),
        Option(doc.getAs[List[String]]("actors").toList.flatten)
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
        "totalNumber" -> t.totalNumber,
        "actors" -> t.actors
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

  implicit object FilmBSONReader extends BSONDocumentReader[Film] {
    def read(doc: BSONDocument): Film = {
      Film(
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONString]("description").map(_.value),
        Option(doc.getAs[List[String]]("actors").toList.flatten),
        doc.getAs[BSONString]("year").map(_.value)
      )
    }
  }


  implicit object FilmBSONWriter extends BSONDocumentWriter[Film] {
    override def write(t: Film): BSONDocument = {
      BSONDocument(
        "title" -> t.title,
        "description" -> t.description,
        "actors" -> t.actors,
        "year" -> t.year
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

  implicit object FilmShortBSONReader extends BSONDocumentReader[FilmShort] {
    def read(doc: BSONDocument): FilmShort = {
      FilmShort(
        doc.getAs[BSONString]("title").get.value
      )
    }
  }


  implicit object FilmShortBSONWriter extends BSONDocumentWriter[FilmShort] {
    override def write(t: FilmShort): BSONDocument = {
      BSONDocument(
        "title" -> t.title
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

  implicit object TVChannelCategoryBSONReader extends BSONDocumentReader[TVChannelCategory] {
    def read(doc: BSONDocument): TVChannelCategory = {
      TVChannelCategory(
        doc.getAs[BSONString]("category").get.value,
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVChannelCategoryBSONWriter extends BSONDocumentWriter[TVChannelCategory] {
    override def write(t: TVChannelCategory): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "category" -> t.category
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
