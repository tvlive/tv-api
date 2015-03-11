package controllers

import controllers.external.{ChannelLong, TVChannelLong}
import models.TVChannel
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVChannelSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  val channel = TVChannelLong("bbc1",
    List("provider1"),
    List("cat1"),
    uriToday = "http://localhost:9000/channel/bbc1/today",
    uriCurrent = "http://localhost:9000/channel/bbc1/current",
    uriLeft = "http://localhost:9000/channel/bbc1/left",
    image = "http://localhost:9000/bbc1.png",
    Some(id))

  "Write and reads" should {
    "transform TVChannel object to json" in {

      val channelJson = Json.toJson(channel)

      (channelJson \ "name").as[String] mustBe "bbc1"
      (channelJson \ "provider").as[List[String]] mustBe Seq("provider1")
      (channelJson \ "category").as[List[String]] mustBe Seq("cat1")
      (channelJson \ "id").as[String] mustBe s"$idString"
      (channelJson \ "uriToday").as[String] mustBe "http://localhost:9000/channel/bbc1/today"
      (channelJson \ "uriCurrent").as[String] mustBe "http://localhost:9000/channel/bbc1/current"
      (channelJson \ "uriLeft").as[String] mustBe "http://localhost:9000/channel/bbc1/left"
      (channelJson \ "image").as[String] mustBe "http://localhost:9000/bbc1.png"
    }

    "transform json to TVChannel object" in {
      Json.parse(
        s"""{"name":"bbc1",
           |"provider":["provider1"],
           |"category":["cat1"],
           |"uriToday":"http://localhost:9000/channel/bbc1/today",
           |"uriCurrent":"http://localhost:9000/channel/bbc1/current",
           |"uriLeft":"http://localhost:9000/channel/bbc1/left",
           |"image":"http://localhost:9000/bbc1.png",
           |"id":\"$idString\"}""".stripMargin)
        .as[TVChannelLong] mustBe channel
    }
  }
}
