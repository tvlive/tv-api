package controllers

import models.TVChannel
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVChannelSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVChannel object to json" in {
      Json.toJson(TVChannel("bbc1",
        List("provider1"),
        List("cat1"),
        Some(id)))
        .toString() mustBe
        s"""{"name":"bbc1","provider":["provider1"],"category":["cat1"],"id":\"$idString\","uriToday":"/tvcontent/channel/bbc1/today","uriCurrent":"/tvcontent/channel/bbc1/current","uriLeft":"/tvcontent/channel/bbc1/left","image":"/bbc1.png"}"""
    }

    "transform json to TVChannel object" in {
      Json.parse(
        s"""{"name":"bbc1",
           |"provider":["provider1"],
           |"category":["cat1"],
           |"id":\"$idString\"}""".stripMargin)
        .as[TVChannel] mustBe TVChannel("bbc1",List("provider1"),List("cat1"),Some(id))
    }
  }
}
