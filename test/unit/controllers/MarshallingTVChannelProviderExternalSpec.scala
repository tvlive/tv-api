package controllers

import controllers.external.TVChannelCategoryExternal
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingTVChannelProviderExternalSpec extends PlaySpec with MustMatchers {

  val cat = TVChannelCategoryExternal("category1")
  "Write and reads" should {
    "transform TVCategory object to json" in {
      Json.toJson(cat).toString() mustBe """{"category":"category1"}"""
    }

    "transform json to BadRequestResponse object" in {
      Json.parse("""{"category":"category1"}""").as[TVChannelCategoryExternal] mustBe cat
    }
  }
}
