package utils

import controllers.external.{TVShort, TVContentShort, TVLong, TVContentLong}
import models._
import org.joda.time.{DateTime, DateTimeZone}

object DomainBuilder {

  object TVShortWithTimeZone {
    def apply(tvContent: TVContent)(implicit host: String, time: TimeProvider): TVContentShort = {
      TVShort(tvContent.copy(start = tvContent.start.withZone(DateTimeZone.forID("Europe/London")),
        end = tvContent.end.withZone(DateTimeZone.forID("Europe/London"))))
    }
  }

  object TVLongWithTimeZone {
    def apply(tvProgram: TVContent)(implicit host: String, time: TimeProvider): TVContentLong = TVLong(tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London"))))
  }

}

object FilmBuilder {
  def apply(channel: String, provider: List[String], start: DateTime, end: DateTime, rating: Option[Double], title: String) =
    TVContent(channel, provider, start, end, rating, None, Some(Film(title, List(), List(), List(), List(), List(), None, None, None, None, None, None)), None)
}

object SeriesBuilder {
  def apply(channel: String, provider: List[String], start: DateTime, end: DateTime, rating: Option[Double], title: String, episodeTitle: String) =
    TVContent(channel, provider, start, end, rating, Some(Series(title, Some(Episode(Some(episodeTitle), None, None, None, None)),
      List(), List(), List(), List(), List(), None, None, None, None, None, None)), None, None)
}

object ProgramBuilder {
  def apply(channel: String, provider: List[String], start: DateTime, end: DateTime, title: String) =
    TVContent(channel, provider, start, end, None, None, None, Some(Program(title, None)))
}

