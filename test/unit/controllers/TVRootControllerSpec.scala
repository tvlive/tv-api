package controllers

import models.{TVChannelCategory, ChannelCategoryRepository, TVChannelProvider, ChannelProviderRepository}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class TVRootControllerSpec extends PlaySpec with MustMatchers with RootSetup {


  "root" should {
    "return the list of tv links that help to start the navigation across the API" in {
      val rootsResponse: Future[SimpleResult] = rootController.roots.apply(FakeRequest())
      status(rootsResponse) mustBe (OK)
      contentType(rootsResponse) mustBe (Some("application/json"))
      val linksInResponse = contentAsString(rootsResponse)
      val links = Json.parse(linksInResponse).as[Seq[String]]
      links mustEqual Seq(
        "/channels",
        "/channels/provider/CABLE",
        "/channels/provider/FREEVIEW",
        "/channels/provider/TERRESTRIAL",
        "/channels/category/FILMS",
        "/channels/category/KID",
        "/channels/category/NEWS"
      )
    }
  }
}

trait RootSetup {
  val tvChannelProvider1 = TVChannelProvider("FREEVIEW")
  val tvChannelProvider2 = TVChannelProvider("TERRESTRIAL")
  val tvChannelProvider3 = TVChannelProvider("CABLE")

  val providerRepo = new ChannelProviderRepository {
    override def findAll(): Future[Seq[TVChannelProvider]] = {
      Future.successful(Seq(tvChannelProvider3, tvChannelProvider1, tvChannelProvider2))
    }
  }

  val tvChannelCategory1 = TVChannelCategory("FILMS")
  val tvChannelCategory2 = TVChannelCategory("KID")
  val tvChannelCategory3 = TVChannelCategory("NEWS")

  val categoryRepo = new ChannelCategoryRepository {

    override def findAll(): Future[Seq[TVChannelCategory]] = {
      Future.successful(Seq(tvChannelCategory1, tvChannelCategory2, tvChannelCategory3))
    }
  }

  class App extends TVRootController {
    override val providerRepository: ChannelProviderRepository = providerRepo
    override val categoryRepository: ChannelCategoryRepository = categoryRepo
  }

  val rootController = new App

}
