package controllers

import models._
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingSeriesSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
  val series = TVContent(
    channel = "bbc1",
    provider = List("FREEVIEW", "SKY"),
    start = now,
    end = now.plusHours(2),
    category = List("documentary"),
    series = Some(Series("serie1",
      episode = Some(Episode(
        episodeTitle = Some("et1"),
        episodePlot = Some("ep1"),
        seasonNumber = Some("12"),
        episodeNumber = Some("2"),
        totalNumber = Some("25"))),
      actors = List("actor1"),
      writer = List("writer1"),
      director = List("director1"),
      genre = List("cat1"),
      language = Some("lang1"),
      country = Some("count1"),
      rating = Some("7"),
      awards = Some("awards"),
      poster = Some("poster1"),
      plot = Some("plot1"),
      year = Some("1976"),
      imdbId = Some("imdbId1"))),
    film = None,
    program = None,
    id = Some(id))

  "Write and reads" should {
    "transform TVContent 'series' object to json" in {

      val tvContentJson = Json.toJson(series)

      (tvContentJson \ "channel").as[String] mustBe "bbc1"
      (tvContentJson \ "channelImageURL").as[String] mustBe "/bbc1.png"
      (tvContentJson \ "provider").as[List[String]] mustBe Seq("FREEVIEW", "SKY")
      (tvContentJson \ "category").as[List[String]] mustBe Seq("documentary")
      (tvContentJson \ "start").as[String] mustBe s"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "end").as[String] mustBe s"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "series" \ "serieTitle").as[String] mustBe "serie1"
      (tvContentJson \ "series" \ "episode" \ "episodeTitle").as[String] mustBe "et1"
      (tvContentJson \ "series" \ "episode" \ "episodePlot").as[String] mustBe "ep1"
      (tvContentJson \ "series" \ "episode" \ "seasonNumber").as[String] mustBe "12"
      (tvContentJson \ "series" \ "episode" \ "episodeNumber").as[String] mustBe "2"
      (tvContentJson \ "series" \ "episode" \ "totalNumber").as[String] mustBe "25"
      (tvContentJson \ "series" \ "actors").as[List[String]] mustBe Seq("actor1")
      (tvContentJson \ "series" \ "writer").as[List[String]] mustBe Seq("writer1")
      (tvContentJson \ "series" \ "director").as[List[String]] mustBe Seq("director1")
      (tvContentJson \ "series" \ "genre").as[List[String]] mustBe Seq("cat1")
      (tvContentJson \ "series" \ "language").as[String] mustBe "lang1"
      (tvContentJson \ "series" \ "country").as[String] mustBe "count1"
      (tvContentJson \ "series" \ "rating").as[String] mustBe "7"
      (tvContentJson \ "series" \ "awards").as[String] mustBe "awards"
      (tvContentJson \ "series" \ "poster").as[String] mustBe "poster1"
      (tvContentJson \ "series" \ "plot").as[String] mustBe "plot1"
      (tvContentJson \ "series" \ "year").as[String] mustBe "1976"
      (tvContentJson \ "series" \ "imdbId").as[String] mustBe "imdbId1"
      (tvContentJson \ "onTimeNow").as[Boolean] mustBe false
      (tvContentJson \ "id").as[String] mustBe s"$idString"
    }

    "transform series json to TVContent 'series' object" in {
      val seriesJson = s"""{"channel":"bbc1",
       |"channelImageURL":"/bbc1.png",
       |"provider":["FREEVIEW","SKY"],
       |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
       |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
       |"category":["documentary"],
       |"series":{
       |  "serieTitle":"serie1",
       |  "episode":{
       |    "episodeTitle":"et1",
       |    "episodePlot":"ep1",
       |    "seasonNumber":"12",
       |    "episodeNumber":"2",
       |    "totalNumber":"25"},
       |    "actors":["actor1"],
       |    "writer":["writer1"],
       |    "director":["director1"],
       |    "genre":["cat1"],
       |    "language":"lang1",
       |     "country":"count1",
       |    "rating":"7",
       |    "awards":"awards",
       |    "poster":"poster1",
       |    "plot":"plot1",
       |    "year":"1976",
       | "imdbId":"imdbId1"},
       |"film":null,
       |"program":null,
       |"onTimeNow":false,
       |"perCentTimeElapsed":null,
       |"id":"$idString"}""".stripMargin

      Json.parse(seriesJson).as[TVContent] mustBe series.copy(
        start =   now.withZone(DateTimeZone.forID("Europe/London")),
        end = now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))
    }
  }
}