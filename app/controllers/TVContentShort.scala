package controllers

import models._
import org.joda.time.{DateTime, Duration}
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider



case class TVContentShort(channel: String,
                          channelImageURL: String,
                          provider: List[String],
                          start: DateTime,
                          end: DateTime,
                          series: Option[SeriesShort],
                          film: Option[FilmShort],
                          program: Option[ProgramShort],
                          onTimeNow: Boolean,
                          perCentTimeElapsed: Option[Long],
                          id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {

  val uriTVContentDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}

case class SeriesShort(serieTitle: String,
                       episode: Option[EpisodeShort],
                       rating: Option[Double],
                       poster: Option[String])

case class EpisodeShort(episodeTitle: Option[String],
                        seasonNumber: Option[String],
                        episodeNumber: Option[String])

case class FilmShort(title: String, rating: Option[Double], poster: Option[String])

case class ProgramShort(title: String)

object TVShort extends TimeProvider with ChannelImageURLBuilder {
  def apply(tvContent: TVContent): TVContentShort = {
    val episode = for {
      s <- tvContent.series
      e <- s.episode
    } yield (e.episodeTitle, e.seasonNumber, e.episodeNumber)

    val es = episode match {
      case None => None
      case Some((et, sn, en)) => Some(EpisodeShort(et, sn, en))
    }

    val channelImageURL = buildUrl(tvContent.channel)

    val onTimeNow = (tvContent.start.isBeforeNow || tvContent.start.isEqualNow) && (tvContent.end.isAfterNow || tvContent.end.isEqualNow)

    val perCentTimeElapsed: Option[Long] = {
      onTimeNow match {
        case true => {
          val initialDuration = new Duration(tvContent.start, tvContent.end).getStandardMinutes
          val currentDuration = new Duration(tvContent.start, currentDate).getStandardMinutes
          Some(currentDuration * 100 / initialDuration)
        }
        case false => None
      }
    }

    TVContentShort(
      tvContent.channel,
      channelImageURL,
      tvContent.provider,
      tvContent.start,
      tvContent.end,
      tvContent.series.map(s => SeriesShort(s.serieTitle, es, s.rating, s.poster)),
      tvContent.film.map(f => FilmShort(f.title, f.rating, f.poster)),
      tvContent.program.map(p => ProgramShort(p.title)),
      onTimeNow,
      perCentTimeElapsed,
      tvContent.id)
  }
}