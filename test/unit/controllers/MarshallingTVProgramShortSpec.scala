package controllers

import models.{FilmShort, SeriesShort, TVContentShort}
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVProgramShortSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014,10,10,10,0,0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

  "Write and reads" should {
    "transform TVProgram object to json" in {
      Json.toJson(TVContentShort("bbc1", now, now.plusHours(2), Some(List("documentary")), Some(SeriesShort("titleSerie")), Some(FilmShort("titleProgram")), Some(id))).toString() mustBe
        s"""{"channel":"bbc1","start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}","end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}","category":["documentary"],"series":{"serieTitle":"titleSerie"},"film":{"title":"titleProgram"},"uriTVProgramDetails":"/tvcontent/$idString","id":"$idString"}"""
    }
    "transform json to TVProgram object" in {
      Json.parse(
        s"""{"channel":"bbc1",
           |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
           |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
           |"category":["documentary"],
           |"series":{"serieTitle":"titleSerie"},
           |"film":{"title":"titleProgram"},
           |"uriTVProgramDetails":"/tvcontent  /$idString","id":"$idString"}""".stripMargin)
        .as[TVContentShort] mustBe
        TVContentShort("bbc1", now.withZone(DateTimeZone.forID("Europe/London")), now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")), Some(List("documentary")), Some(SeriesShort("titleSerie")), Some(FilmShort("titleProgram")), Some(id))
    }
  }
}
