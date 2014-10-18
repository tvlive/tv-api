package controllers

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingBadResponseResponseSpec extends PlaySpec with MustMatchers {

  "Write and reads" should {
    "transform BadRequestResponse object to json" in {
      Json.toJson(BadRequestResponse("some reson")).toString() mustBe """{"reason":"some reson","status":400}"""
    }

    "transform json to BadRequestResponse object" in {
      Json.parse("""{"reason":"some reson","status":400}""").as[BadRequestResponse] mustBe BadRequestResponse("some reson")
    }
  }
}
