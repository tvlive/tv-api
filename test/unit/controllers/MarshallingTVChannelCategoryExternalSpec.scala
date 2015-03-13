package controllers

import models.TVChannelCategory
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingTVChannelCategoryExternalSpec extends PlaySpec with MustMatchers {

  "Write and reads" should {
    "transform TVCategory object to json" in {
      Json.toJson(TVChannelCategory("category1")).toString() mustBe """{"category":"category1"}"""
    }

    "transform json to BadRequestResponse object" in {
      Json.parse("""{"category":"category1"}""").as[BadRequestResponse] mustBe BadRequestResponse("some reson")
    }
  }
}
