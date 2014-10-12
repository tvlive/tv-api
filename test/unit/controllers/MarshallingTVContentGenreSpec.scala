package controllers

import models.TVContentGenre
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVContentGenreSpec extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVContentGenre object to json" in {
      Json.toJson(TVContentGenre("ENTERTAINMENT", Some(id))).toString() mustBe s"""{"genre":"ENTERTAINMENT","id":\"$idString\"}"""
    }

    "transform json to TVContentGenre object" in {
      Json.parse(s"""{"genre":"ENTERTAINMENT","id":\"$idString\"}""").as[TVContentGenre] mustBe TVContentGenre("ENTERTAINMENT", Some(id))
    }
  }
}