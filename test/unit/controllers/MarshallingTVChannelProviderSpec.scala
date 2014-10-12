package controllers

import models.TVChannelProvider
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVChannelProviderSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVChannelProvider object to json" in {
      Json.toJson(TVChannelProvider("Provider1", Some(id))).toString() mustBe s"""{"provider":"Provider1","id":\"$idString\"}"""
    }

    "transform json to TVChannelProvider object" in {
      Json.parse(s"""{"provider":"Provider1","id":\"$idString\"}""").as[TVChannelProvider] mustBe TVChannelProvider("Provider1", Some(id))
    }
  }
}
