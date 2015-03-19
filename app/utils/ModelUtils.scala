package utils

import controllers.external.{EpisodeShort, TVShort}
import TVShort._
import models.TVContent
import org.joda.time.Duration

trait ModelUtils {

  def epishoShort(tvContent: TVContent): Option[EpisodeShort] = {
    val episode = for {
      s <- tvContent.series
      e <- s.episode
    } yield (e.episodeTitle, e.seasonNumber, e.episodeNumber)

    episode match {
      case None => None
      case Some((et, sn, en)) => Some(EpisodeShort(et, sn, en))
    }
  }

  def isNowShowing(tvContent: TVContent)(implicit time: TimeProvider): Boolean =
    (tvContent.start.isBefore(time.currentDate())  || tvContent.start.isEqual(time.currentDate())) &&
      (tvContent.end.isAfter(time.currentDate()) || tvContent.end.isEqual(time.currentDate()))

  def calculateElapsed(tvContent: TVContent)(implicit time: TimeProvider): Option[Long] = {
    val initialDuration = new Duration(tvContent.start, tvContent.end).getStandardMinutes
    val currentDuration = new Duration(tvContent.start, time.currentDate).getStandardMinutes
    Some(currentDuration * 100 / initialDuration)
  }
}
