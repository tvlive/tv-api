package utils

import org.joda.time.{DateTimeZone, DateTime}

trait TimeProvider {

  def currentDate() = new DateTime(DateTimeZone.forID("UTC"))

}
