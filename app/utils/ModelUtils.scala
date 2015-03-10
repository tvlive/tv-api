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

  def isNowShowing(tvContent: TVContent): Boolean =
    (tvContent.start.isBeforeNow || tvContent.start.isEqualNow) &&
      (tvContent.end.isAfterNow || tvContent.end.isEqualNow)

  def calculateElapsed(tvContent: TVContent): Option[Long] = {
    val initialDuration = new Duration(tvContent.start, tvContent.end).getStandardMinutes
    val currentDuration = new Duration(tvContent.start, currentDate).getStandardMinutes
    Some(currentDuration * 100 / initialDuration)
  }
}
