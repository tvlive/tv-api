package controllers

import models.{Film, Series, TVProgram}
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVProgramSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014,10,10,10,0,0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

  "Write and reads" should {
    "transform TVProgram object to json" in {
      Json.toJson(TVProgram("bbc1", now, now.plusHours(2), Some(List("documentary")),
        Some(Series("serie1", "ep1", None, None, None, None, Some(List("actor1")))), Some(Film("program1", None, Some(List()), Some("2014"))), Some(id))).toString() mustBe
        s"""{"channel":"bbc1","start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}","end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}","category":["documentary"],"series":{"serieTitle":"serie1","episodeTitle":"ep1","actors":["actor1"]},"film":{"title":"program1","actors":[],"year":"2014"},"id":"$idString"}""".stripMargin
    }
    "transform json to TVProgram object" in {
      Json.parse(
        s"""{"channel":"bbc1",
           |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
           |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
           |"category":["documentary"],
           |"series":{"serieTitle":"serie1","episodeTitle":"ep1","actors":["actor1"]},
           |"film":{"title":"program1","actors":[],"year":"2014"},
           |"id":"$idString"}""".stripMargin)
        .as[TVProgram] mustBe TVProgram("bbc1", now.withZone(DateTimeZone.forID("Europe/London")), now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")),
        Some(List("documentary")), Some(Series("serie1", "ep1", None, None, None, None, Some(List("actor1")))), Some(Film("program1", None, Some(List()), Some("2014"))), Some(id))
    }
  }
}
