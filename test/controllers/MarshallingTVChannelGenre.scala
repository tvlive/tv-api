package controllers

import models.TVChannelGenre
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVChannelGenre extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVChannelGenre object to json" in {
      Json.toJson(TVChannelGenre("ENTERTAINMENT", Some(id))).toString() mustBe s"""{"genre":"ENTERTAINMENT","id":\"$idString\"}"""
    }

    "transform json to TVChannel object" in {
      Json.parse(s"""{"genre":"ENTERTAINMENT","id":\"$idString\"}""").as[TVChannelGenre] mustBe TVChannelGenre("ENTERTAINMENT", Some(id))
    }
  }
}
