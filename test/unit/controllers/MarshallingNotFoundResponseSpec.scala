package controllers

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class MarshallingNotFoundResponseSpec extends PlaySpec with MustMatchers {

  "Write and reads" should {
    "transform NotFoundResponse object to json" in {
      Json.toJson(NotFoundResponse("some reson")).toString() mustBe """{"reason":"some reson","status":404}"""
    }

    "transform json to NotFoundResponse object" in {
      Json.parse("""{"reason":"some reson","status":404}""").as[NotFoundResponse] mustBe NotFoundResponse("some reson")
    }
  }
}
