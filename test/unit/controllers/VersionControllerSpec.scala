package controllers

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.{FakeApplication, WithApplication, FakeRequest}
import play.api.test.Helpers._

import scala.concurrent.Future

class VersionControllerSpec extends PlaySpec with MustMatchers {



  "VersionController" should {
    "provide the stable version deployed" in new WithApplication(FakeApplication(additionalConfiguration = Map("app.version" -> "0.1.0"))) {

      val versionResult: Future[SimpleResult] = VersionController.fetchVersion().apply(FakeRequest())
      status(versionResult) mustBe (OK)
      contentType(versionResult) mustBe (Some("application/json"))
      val versionInResponse = contentAsString(versionResult)
      val version = Json.parse(versionInResponse).as[Version]
      version mustEqual Version("0.1.0", "STABLE")

    }

    "provide the unstable version deployed" in new WithApplication(FakeApplication(additionalConfiguration = Map("app.version" -> "0.1.0-SNAPSHOT"))) {

      val versionResult: Future[SimpleResult] = VersionController.fetchVersion().apply(FakeRequest())
      status(versionResult) mustBe (OK)
      contentType(versionResult) mustBe (Some("application/json"))
      val versionInResponse = contentAsString(versionResult)
      val version = Json.parse(versionInResponse).as[Version]
      version mustEqual Version("0.1.0-SNAPSHOT", "UNSTABLE")

    }

    "return INTERNAL_SERVER_ERROR when there is no version defined" in new WithApplication() {

      val versionResult: Future[SimpleResult] = VersionController.fetchVersion().apply(FakeRequest())
      status(versionResult) mustBe (INTERNAL_SERVER_ERROR)
      contentType(versionResult) mustBe (Some("application/json"))
      val versionInResponse = contentAsString(versionResult)
      val internalErrorServer = Json.parse(versionInResponse).as[InternalErrorServerResponse]
      internalErrorServer mustEqual InternalErrorServerResponse("No version found")
    }
  }

}
