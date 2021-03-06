package controllers

import controllers.external.{FilmLong, TVContentLong}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingFilmSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))
  val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
  val film = TVContentLong(
    channel = "bbc1",
    channelImageURL = "http://beta.tvlive.io/bbc1.png",
    provider = List("FREEVIEW", "SKY"),
    start = now,
    end = now.plusHours(2),
    rating = Some(8),
    film = Some(FilmLong("film2",
      actors = List("actor2"),
      writer = List("writer2"),
      director = List("director2"),
      genre = List("cat2"),
      country = List("count2"),
      language = Some("lang2"),
      awards = Some("awards2"),
      poster = Some("poster2"),
      posterImdb = Some("posterImdb2"),
      plot = Some("plot2"),
      year = Some("1977"),
      imdbId = Some("imdbId1"))),
    series = None,
    program = None,
    onTimeNow = false,
    minutesLeft = Some(120))


  "Write and reads" should {
    "transform TVContentLong 'film' object to json" in {

      val tvContentJson = Json.toJson(film)

      (tvContentJson \ "channel").as[String] mustBe "bbc1"
      (tvContentJson \ "channelImageURL").as[String] mustBe "http://beta.tvlive.io/bbc1.png"
      (tvContentJson \ "provider").as[List[String]] mustBe Seq("FREEVIEW", "SKY")
      (tvContentJson \ "start").as[String] mustBe s"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "end").as[String] mustBe s"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}"
      (tvContentJson \ "rating").as[Double] mustBe 8
      (tvContentJson \ "film" \ "title").as[String] mustBe "film2"
      (tvContentJson \ "film" \ "actors").as[List[String]] mustBe Seq("actor2")
      (tvContentJson \ "film" \ "writer").as[List[String]] mustBe Seq("writer2")
      (tvContentJson \ "film" \ "director").as[List[String]] mustBe Seq("director2")
      (tvContentJson \ "film" \ "genre").as[List[String]] mustBe Seq("cat2")
      (tvContentJson \ "film" \ "country").as[List[String]] mustBe Seq("count2")
      (tvContentJson \ "film" \ "language").as[String] mustBe "lang2"
      (tvContentJson \ "film" \ "awards").as[String] mustBe "awards2"
      (tvContentJson \ "film" \ "poster").as[String] mustBe "poster2"
      (tvContentJson \ "film" \ "posterImdb").as[String] mustBe "posterImdb2"
      (tvContentJson \ "film" \ "plot").as[String] mustBe "plot2"
      (tvContentJson \ "film" \ "year").as[String] mustBe "1977"
      (tvContentJson \ "film" \ "imdbId").as[String] mustBe "imdbId1"
      (tvContentJson \ "onTimeNow").as[Boolean] mustBe false
      (tvContentJson \ "minutesLeft").as[Double] mustBe 120
    }

    "transform film json to TVContentLong 'film' object" in {
      val filmJson = s"""{"channel":"bbc1",
                        |"channelImageURL":"http://beta.tvlive.io/bbc1.png",
                        |"provider":["FREEVIEW","SKY"],
                        |"start":"${fmt.print(now.withZone(DateTimeZone.forID("Europe/London")))}",
                        |"end":"${fmt.print(now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))}",
                        |"rating":8,
                        |"series":null,
                       |"film":{
                       |"title":"film2",
                       |"actors":["actor2"],
                       |"writer":["writer2"],
                       |"director":["director2"],
                       |"genre":["cat2"],
                       |"country":["count2"],
                       |"language":"lang2",
                       |"awards":"awards2",
                       |"poster":"poster2",
                       |"posterImdb":"posterImdb2",
                       |"plot":"plot2",
                       |"year":"1977",
                       |"imdbId":"imdbId1"},
                       |"program":null,
                       |"onTimeNow":false,
                       |"minutesLeft":120}""".stripMargin

      Json.parse(filmJson).as[TVContentLong] mustBe film.copy(
        start = now.withZone(DateTimeZone.forID("Europe/London")),
        end = now.plusHours(2).withZone(DateTimeZone.forID("Europe/London")))
    }
  }
}