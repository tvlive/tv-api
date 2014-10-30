package utils

import controllers.{ProgramShort, SeriesShort, FilmShort, TVContentShort}
import models._
import org.joda.time.DateTimeZone

object DomainBuilder {

  object TVShortWithTimeZone {
    def apply(tvProgram: TVContent): TVContentShort = TVContentShort(tvProgram.channel,
      tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.end.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.category, tvProgram.series.map(s => SeriesShort(s.serieTitle)),
      tvProgram.film.map(p => FilmShort(p.title)),
      tvProgram.program.map(p => ProgramShort(p.title)),
      tvProgram.id)
  }


  object TVProgramWithTimeZone {
    def apply(tvProgram: TVContent): TVContent = tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London")))
  }
}
