package utils

import models.{FilmShort, SeriesShort, TVProgramShort, TVProgram}
import org.joda.time.DateTimeZone

object DomainBuilder {

  object TVShort {
    def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel,
      tvProgram.start,
      tvProgram.end,
      tvProgram.category, tvProgram.series.map(s => SeriesShort(s.serieTitle)),
      tvProgram.film.map(p => FilmShort(p.title)), tvProgram.id)
  }

  object TVShortWithTimeZone {
    def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel,
      tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.end.withZone(DateTimeZone.forID("Europe/London")),
      tvProgram.category, tvProgram.series.map(s => SeriesShort(s.serieTitle)),
      tvProgram.film.map(p => FilmShort(p.title)), tvProgram.id)
  }


  object TVProgramWithTimeZone {
    def apply(tvProgram: TVProgram): TVProgram = tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London")))
  }
}
