package controllers

import models.TVChannelCategory
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingLinkSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVChannelCategory object to json" in {
      Json.toJson(TVChannelCategory("ENTERTAINMENT", Some(id))).toString() mustBe s"""{"category":"ENTERTAINMENT","id":\"$idString\"}"""
    }

    "transform json to TVChannelCategory object" in {
      Json.parse(s"""{"category":"ENTERTAINMENT","id":\"$idString\"}""").as[TVChannelCategory] mustBe TVChannelCategory("ENTERTAINMENT", Some(id))
    }
  }
}
