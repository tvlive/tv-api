package controllers.external

import models._
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID
import utils.{URLBuilder, ModelUtils, TimeProvider}


case class TVContentLong(channel: String,
                         channelImageURL: String,
                         provider: List[String],
                         start: DateTime,
                         end: DateTime,
                         series: Option[SeriesLong],
                         film: Option[FilmLong],
                         program: Option[ProgramLong],
                         onTimeNow: Boolean,
                         perCentTimeElapsed: Option[Long])

case class EpisodeLong(episodeTitle: Option[String],
                       episodePlot: Option[String],
                       seasonNumber: Option[String],
                       episodeNumber: Option[String],
                       totalNumber: Option[String])

case class SeriesLong(serieTitle: String,
                      episode: Option[EpisodeLong],
                      actors: List[String],
                      writer: List[String],
                      director: List[String],
                      genre: List[String],
                      country: List[String],
                      language: Option[String],
                      rating: Option[Double],
                      awards: Option[String],
                      poster: Option[String],
                      plot: Option[String],
                      year: Option[String],
                      imdbId: Option[String])


case class FilmLong(title: String,
                    actors: List[String],
                    writer: List[String],
                    director: List[String],
                    genre: List[String],
                    country: List[String],
                    language: Option[String],
                    rating: Option[Double],
                    awards: Option[String],
                    poster: Option[String],
                    plot: Option[String],
                    year: Option[String],
                    imdbId: Option[String])


case class ProgramLong(title: String, plot: Option[String])


object TVLong extends URLBuilder with ModelUtils {
  def apply(tvContent: TVContent)(implicit host: String, time: TimeProvider): TVContentLong = {
    val onTimeNow = isNowShowing(tvContent)

    val perCentTimeElapsed = onTimeNow match {
      case true => calculateElapsed(tvContent)
      case false => None
    }

    TVContentLong(
      tvContent.channel,
      buildImageUrl(host, "/", tvContent.channel),
      tvContent.provider,
      tvContent.start,
      tvContent.end,
      tvContent.series.map(s => SLong(s)),
      tvContent.film.map(f => FLong(f)),
      tvContent.program.map(p => PLong(p)),
      onTimeNow,
      perCentTimeElapsed)

  }
}

object SLong {
  def apply(s: Series): SeriesLong = {
    SeriesLong(s.serieTitle,
      s.episode.map(e => ELong(e)),
      s.actors, s.writer, s.director, s.genre, s.country, s.language, s.rating,
      s.awards, s.poster, s.plot, s.year, s.imdbId)
  }
}

object ELong {
  def apply(e: Episode): EpisodeLong = EpisodeLong(e.episodeTitle, e.episodePlot, e.seasonNumber, e.episodeNumber, e.totalNumber)
}

object FLong {
  def apply(f: Film): FilmLong = FilmLong(f.title, f.actors, f.writer, f.director, f.genre, f.country, f.language, f.rating,
    f.awards, f.poster, f.plot, f.year, f.imdbId)
}

object PLong {
  def apply(p: Program): ProgramLong = ProgramLong(p.title, p.plot)
}
