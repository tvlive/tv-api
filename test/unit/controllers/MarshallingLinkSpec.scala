package controllers

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingLinkSpec extends PlaySpec with MustMatchers {
  "Write and reads" should {
    "transform Link object to json" in {
      Json.toJson(Link("/some/url", "some description")).toString() mustBe s"""{"uri":"/some/url","description":"some description"}"""
    }

    "transform json to TVChannelCategory object" in {
      Json.parse(s"""{"uri":"/some/url","description":"some description"}""").as[Link] mustBe Link("/some/url", "some description")
    }
  }
}
