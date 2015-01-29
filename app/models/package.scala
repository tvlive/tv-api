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
        doc.getAs[List[String]]("provider").toList.flatten,
        new DateTime(doc.getAs[BSONDateTime]("start").get.value, DateTimeZone.forID("UTC")),
        new DateTime(doc.getAs[BSONDateTime]("end").get.value, DateTimeZone.forID("UTC")),
        doc.getAs[List[String]]("category").toList.flatten,
        doc.getAs[BSONDocument]("series").map(SerieBSONReader.read(_)),
        doc.getAs[BSONDocument]("film").map(FilmBSONReader.read(_)),
        doc.getAs[BSONDocument]("program").map(ProgramBSONReader.read(_)),
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }

  implicit object TVContentContentBSONWriter extends BSONDocumentWriter[TVContent] {
    override def write(t: TVContent): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channel" -> t.channel,
        "provider" -> t.provider,
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
        doc.getAs[BSONDocument]("episode").map(EpisodeBSONReader.read(_)),
        doc.getAs[List[String]]("actors").toList.flatten,
        doc.getAs[List[String]]("writer").toList.flatten,
        doc.getAs[List[String]]("director").toList.flatten,
        doc.getAs[List[String]]("genre").toList.flatten,
        doc.getAs[BSONString]("language").map(_.value),
        doc.getAs[BSONString]("country").map(_.value),
        doc.getAs[BSONString]("rating").map(_.value),
        doc.getAs[BSONString]("awards").map(_.value),
        doc.getAs[BSONString]("poster").map(_.value),
        doc.getAs[BSONString]("plot").map(_.value),
        doc.getAs[BSONString]("year").map(_.value),
        doc.getAs[BSONString]("imdbId").map(_.value))
    }
  }


  implicit object SerieBSONWriter extends BSONDocumentWriter[Series] {
    override def write(t: Series): BSONDocument = {
      BSONDocument(
        "serieTitle" -> t.serieTitle,
        "episode" -> t.episode,
        "actors" -> t.actors,
        "writer" -> t.writer,
        "director" -> t.director,
        "genre" -> t.genre,
        "language" -> t.language,
        "country" -> t.country,
        "rating" -> t.rating,
        "awards" -> t.awards,
        "poster" -> t.poster,
        "plot" -> t.plot,
        "year" -> t.year
      )
    }
  }

  implicit object EpisodeBSONReader extends BSONDocumentReader[Episode] {
    def read(doc: BSONDocument): Episode = {
      Episode(
        doc.getAs[BSONString]("episodeTitle").map(_.value),
        doc.getAs[BSONString]("episodePlot").map(_.value),
        doc.getAs[BSONString]("seasonNumber").map(_.value),
        doc.getAs[BSONString]("episodeNumber").map(_.value),
        doc.getAs[BSONString]("totalNumber").map(_.value))
    }
  }


  implicit object EpisodeBSONWriter extends BSONDocumentWriter[Episode] {
    override def write(t: Episode): BSONDocument = {
      BSONDocument(
        "episodeTitle" -> t.episodeTitle,
        "episodePlot" -> t.episodePlot,
        "seasonNumber" -> t.seasonNumber,
        "episodeNumber" -> t.episodeNumber,
        "totalNumber" -> t.totalNumber)
    }
  }


  implicit object FilmBSONReader extends BSONDocumentReader[Film] {
    def read(doc: BSONDocument): Film = {
      Film(
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[List[String]]("actors").toList.flatten,
        doc.getAs[List[String]]("writer").toList.flatten,
        doc.getAs[List[String]]("director").toList.flatten,
        doc.getAs[List[String]]("genre").toList.flatten,
        doc.getAs[BSONString]("language").map(_.value),
        doc.getAs[BSONString]("country").map(_.value),
        doc.getAs[BSONString]("rating").map(_.value),
        doc.getAs[BSONString]("awards").map(_.value),
        doc.getAs[BSONString]("poster").map(_.value),
        doc.getAs[BSONString]("plot").map(_.value),
        doc.getAs[BSONString]("year").map(_.value),
        doc.getAs[BSONString]("imdbId").map(_.value))
    }
  }


  implicit object FilmBSONWriter extends BSONDocumentWriter[Film] {
    override def write(t: Film): BSONDocument = {
      BSONDocument(
        "title" -> t.title,
        "plot" -> t.plot,
        "actors" -> t.actors,
        "writer" -> t.writer,
        "director" -> t.director,
        "genre" -> t.genre,
        "language" -> t.language,
        "country" -> t.country,
        "rating" -> t.rating,
        "awards" -> t.awards,
        "poster" -> t.poster,
        "plot" -> t.plot,
        "year" -> t.year)
    }
  }

  implicit object ProgramBSONReader extends BSONDocumentReader[Program] {
    def read(doc: BSONDocument): Program = {
      Program(
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONString]("plot").map(_.value)
      )
    }
  }


  implicit object ProgramBSONWriter extends BSONDocumentWriter[Program] {
    override def write(t: Program): BSONDocument = {
      BSONDocument(
        "title" -> t.title,
        "plot" -> t.plot
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

  implicit object TVChannelProviderBSONReader extends BSONDocumentReader[TVChannelProvider] {
    def read(doc: BSONDocument): TVChannelProvider = {
      TVChannelProvider(
        doc.getAs[BSONString]("provider").get.value,
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVChannelProviderBSONWriter extends BSONDocumentWriter[TVChannelProvider] {
    override def write(t: TVChannelProvider): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "provider" -> t.provider
      )
    }
  }
}
