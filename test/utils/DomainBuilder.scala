package utils

import models.{ProgramShort, SerieShort, TVProgramShort, TVProgram}
import org.joda.time.DateTimeZone

object DomainBuilder {

  object TVShort {
    def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel,
      tvProgram.start,
      tvProgram.end,
      tvProgram.category, tvProgram.series.map(s => SerieShort(s.serieTitle)),
      tvProgram.program.map(p => ProgramShort(p.title)), tvProgram.id)
  }

  object TVShortWithTimeZone {
    def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel,
      tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.end.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.category, tvProgram.series.map(s => SerieShort(s.serieTitle)),
      tvProgram.program.map(p => ProgramShort(p.title)), tvProgram.id)
  }


  object TVProgramWithTimeZone {
    def apply(tvProgram: TVProgram): TVProgram = tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London")))
  }
}
