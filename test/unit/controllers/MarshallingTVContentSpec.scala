package controllers

import models._
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVContentSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014,10,10,10,0,0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

  "Write and reads" should {
    "transform TVContent object to json" in {
      Json.toJson(TVContent("bbc1", List("FREEVIEW", "SKY"), now, now.plusHours(2), Some(List("documentary")),
        Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), Some(List("actor1")))),
        Some(Film("program1", None, Some(List()), Some("2014"))),
        Some(Program("program1", Some("d1"))),
        Some(id))).toString() mustBe
        s"""{"channel":"bbc1","channelImageURL":"/bbc1.png","provider":["FREEVIEW","SKY"],"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}","end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}","category":["documentary"],"series":{"serieTitle":"serie1","episodeTitle":"ep1","actors":["actor1"]},"film":{"title":"program1","actors":[],"year":"2014"},"program":{"title":"program1","description":"d1"},"onTimeNow":false,"perCentTimeElapsed":null,"id":"$idString"}""".stripMargin
    }
    "transform json to TVContent object" in {
      Json.parse(
        s"""{"channel":"bbc1",
           |"provider":["FREEVIEW","SKY"],
           |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
           |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
           |"category":["documentary"],
           |"series":{"serieTitle":"serie1","episodeTitle":"ep1","actors":["actor1"]},
           |"film":{"title":"program1","actors":[],"year":"2014"},
           |"program":{"title":"program1","description":"d1"},
           |"id":"$idString"}""".stripMargin)
        .as[TVContent] mustBe TVContent("bbc1",
        List("FREEVIEW", "SKY"),
        now.withZone(DateTimeZone.forID("Europe/London")),
        now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")),
        Some(List("documentary")),
        Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), Some(List("actor1")))),
        Some(Film("program1", None, Some(List()), Some("2014"))),
        Some(Program("program1", Some("d1"))),
        Some(id))
    }
  }
}
