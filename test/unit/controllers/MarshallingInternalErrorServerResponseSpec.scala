package controllers

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingInternalErrorServerResponseSpec extends PlaySpec with MustMatchers {

  "Write and reads" should {
    "transform InternalErrorServerResponse object to json" in {
      Json.toJson(InternalErrorServerResponse("some exceptional situation")).toString() mustBe """{"reason":"some exceptional situation","status":500}"""
    }

    "transform json to TVChannelCategory object" in {
      Json.parse("""{"reason":"some exceptional situation","status":500}""").as[InternalErrorServerResponse] mustBe InternalErrorServerResponse("some exceptional situation")
    }
  }
}
