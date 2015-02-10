package controllers

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVContentShortSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
  val tvContentShort = TVContentShort(
    channel = "bbc1",
    channelImageURL = "/bbc1.png",
    provider = List("FREEVIEW", "SKY"),
    start = now,
    end = now.plusHours(2),
    series = Some(SeriesShort("titleSerie", Some(EpisodeShort(Some("episodeTitle1"), Some("1"), Some("2"))), Some(7))),
    film = Some(FilmShort("titleFilm", Some(8))),
    program = Some(ProgramShort("titleProgram")),
    onTimeNow = true,
    perCentTimeElapsed = Some(10),
    id = Some(id))

  "Write and reads" should {
    "transform TVContent short object to json" in {
      val tvContentJson = Json.toJson(tvContentShort)
      (tvContentJson \ "channel").as[String] mustBe "bbc1"
      (tvContentJson \ "channelImageURL").as[String] mustBe "/bbc1.png"
      (tvContentJson \ "provider").as[List[String]] mustBe Seq("FREEVIEW", "SKY")
      (tvContentJson \ "start").as[String] mustBe s"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "end").as[String] mustBe s"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "series" \ "serieTitle").as[String] mustBe "titleSerie"
      (tvContentJson \ "series" \ "episode" \ "episodeTitle").as[String] mustBe "episodeTitle1"
      (tvContentJson \ "series" \ "episode" \ "seasonNumber").as[String] mustBe "1"
      (tvContentJson \ "series" \ "episode" \ "episodeNumber").as[String] mustBe "2"
      (tvContentJson \ "series" \ "rating").as[Double] mustBe 7
      (tvContentJson \ "film" \ "title").as[String] mustBe "titleFilm"
      (tvContentJson \ "film" \ "rating").as[Double] mustBe 8
      (tvContentJson \ "program" \ "title").as[String] mustBe "titleProgram"
      (tvContentJson \ "uriTVContentDetails").as[String] mustBe s"/tvcontent/$idString"
      (tvContentJson \ "onTimeNow").as[Boolean] mustBe true
      (tvContentJson \ "perCentTimeElapsed").as[Int] mustBe 10
      (tvContentJson \ "id").as[String] mustBe s"$idString"
    }

    "transform json to TVProgram short object" in {
      val tvContentShortJson =
        s"""{"channel":"bbc1",
          |"channelImageURL":"/bbc1.png",
          |"provider":["FREEVIEW","SKY"],
          |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
          |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
          |"series":{
          | "serieTitle":"titleSerie",
          | "episode":{"episodeTitle":"episodeTitle1","seasonNumber":"1","episodeNumber":"2"},
          | "rating":7},
          |"film":{"title":"titleFilm",
          | "rating":8},
          |"program":{"title":"titleProgram"},
          |"uriTVContentDetails":"/tvcontent/54cbe9810100000100af8be0",
          |"onTimeNow":true,
          |"perCentTimeElapsed":10,
          |"id":"$idString"}""".stripMargin

      Json.parse(tvContentShortJson).as[TVContentShort] mustBe tvContentShort.copy(
        start = now.withZone(DateTimeZone.forID("Europe/London")),
        end = now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))
    }
  }
}
