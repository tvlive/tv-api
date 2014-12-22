package utils

import controllers.{ProgramShort, SeriesShort, FilmShort, TVContentShort}
import models._
import org.joda.time.DateTimeZone

object DomainBuilder {

  object TVShortWithTimeZone {
    def apply(tvContent: TVContent): TVContentShort = TVContentShort(
      tvContent.channel,
      tvContent.provider,
      tvContent.start.withZone(DateTimeZone.forID("Europe/London")),
      tvContent.end.withZone(DateTimeZone.forID("Europe/London")),
      tvContent.category, tvContent.series.map(s => SeriesShort(s.serieTitle)),
      tvContent.film.map(p => FilmShort(p.title)),
      tvContent.program.map(p => ProgramShort(p.title)),
      tvContent.onTimeNow,
      tvContent.perCentTimeElapsed,
      tvContent.id)
  }


  object TVProgramWithTimeZone {
    def apply(tvProgram: TVContent): TVContent = tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London")))
  }
}
