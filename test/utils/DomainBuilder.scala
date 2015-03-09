package utils

import controllers.external.{TVShort, TVContentShort, TVLong, TVContentLong}
import models._
import org.joda.time.DateTimeZone

object DomainBuilder {

  object TVShortWithTimeZone {
    def apply(tvContent: TVContent)(implicit host:String): TVContentShort = {
      TVShort(tvContent.copy(start = tvContent.start.withZone(DateTimeZone.forID("Europe/London")),
        end = tvContent.end.withZone(DateTimeZone.forID("Europe/London"))))(host)
    }
  }

  object TVLongWithTimeZone {
    def apply(tvProgram: TVContent)(implicit host:String): TVContentLong = TVLong(tvProgram.copy(
      start = tvProgram.start.withZone(DateTimeZone.forID("Europe/London")),
      end = tvProgram.end.withZone(DateTimeZone.forID("Europe/London"))))(host)
  }

}
