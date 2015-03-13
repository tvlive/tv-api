package controllers

import controllers.external.{TVChannelProviderExternal, TVChannelCategoryExternal}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingTVChannelProviderExternalSpec extends PlaySpec with MustMatchers {

  val prov = TVChannelProviderExternal("provider1")
  "Write and reads" should {
    "transform TVChannelProviderExternal object to json" in {
      Json.toJson(prov).toString() mustBe """{"provider":"provider1"}"""
    }

    "transform json to TVChannelProviderExternal object" in {
      Json.parse("""{"provider":"provider1"}""").as[TVChannelProviderExternal] mustBe prov
    }
  }
}
