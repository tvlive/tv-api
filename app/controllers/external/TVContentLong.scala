package controllers.external

import models._
import org.joda.time.DateTime
import utils.{ModelUtils, TimeProvider, URLBuilder}


case class TVContentLong(channel: String,
                         channelImageURL: String,
                         provider: List[String],
                         start: DateTime,
                         end: DateTime,
                         rating: Option[Double],
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
                      awards: Option[String],
                      poster: Option[String],
                      posterImdb: Option[String],
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
                    awards: Option[String],
                    poster: Option[String],
                    posterImdb: Option[String],
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
      tvContent.rating,
      tvContent.series.map(s => SLong(s)),
      tvContent.film.map(f => FLong(f)),
      tvContent.program.map(p => PLong(p)),
      onTimeNow,
      perCentTimeElapsed)

  }
}

object SLong extends URLBuilder {
  def apply(s: Series)(implicit host: String): SeriesLong = {
    val poster = s.imdbId.map(buildUrl(host,"/images/",_))
    SeriesLong(s.serieTitle,
      s.episode.map(e => ELong(e)),
      s.actors, s.writer, s.director, s.genre, s.country, s.language,
      s.awards, poster, s.posterImdb, s.plot, s.year, s.imdbId)
  }
}

object ELong {
  def apply(e: Episode): EpisodeLong = EpisodeLong(e.episodeTitle, e.episodePlot, e.seasonNumber, e.episodeNumber, e.totalNumber)
}

object FLong extends URLBuilder {
  def apply(f: Film)(implicit host: String): FilmLong = {
    val poster = f.imdbId.map(buildUrl(host,"/images/",_))
    FilmLong(f.title, f.actors, f.writer, f.director, f.genre, f.country, f.language,
      f.awards, poster, f.posterImdb, f.plot, f.year, f.imdbId)
  }
}

object PLong {
  def apply(p: Program): ProgramLong = ProgramLong(p.title, p.plot)
}
