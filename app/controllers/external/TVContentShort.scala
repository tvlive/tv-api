package controllers.external

import models._
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID
import utils.{URLBuilder, ModelUtils, TimeProvider}



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
                          uriTVContentDetails: String,
                          id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {


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

object TVShort extends TimeProvider with URLBuilder with ModelUtils {
  def apply(tvContent: TVContent)(implicit host: String): TVContentShort = {
    val es = epishoShort(tvContent)

    val onTimeNow = isNowShowing(tvContent)

    val perCentTimeElapsed = onTimeNow match {
        case true => calculateElapsed(tvContent)
        case false => None
      }

    val uriTVContentDetails = buildUrl(host, controllers.routes.TVContentController.tvContentDetails(tvContent.id.get.stringify).url)

    TVContentShort(
      tvContent.channel,
      buildImageUrl(host, "/", tvContent.channel),
      tvContent.provider,
      tvContent.start,
      tvContent.end,
      tvContent.series.map(s => SeriesShort(s.serieTitle, es, s.rating, s.poster)),
      tvContent.film.map(f => FilmShort(f.title, f.rating, f.poster)),
      tvContent.program.map(p => ProgramShort(p.title)),
      onTimeNow,
      perCentTimeElapsed,
      uriTVContentDetails,
      tvContent.id)
  }
}