package controllers

import models.{ChannelCategoryRepository, ChannelProviderRepository, TVChannelCategory, TVChannelProvider}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

import scala.concurrent.Future

class TVRootControllerSpec extends PlaySpec with MustMatchers with RootSetup  {


  "root" should {
    "return the list of tv links that help to start the navigation across the API" in  new WithApplication() {
      val rootsResponse: Future[SimpleResult] = rootController.roots.apply(FakeRequest())
      status(rootsResponse) mustBe (OK)
      contentType(rootsResponse) mustBe (Some("application/json"))
      val linksInResponse = contentAsString(rootsResponse)
      val links = Json.parse(linksInResponse).as[Seq[Link]]
      links mustEqual Seq(
        Link("/channels", "List of all the channels"),
        Link("/providers/channels", "List of all the channel TV providers"),
        Link("/categories/channels", "List of all the channel TV categories"),
        Link("/channels/provider/CABLE", "List of channels by provider: CABLE"),
        Link("/channels/provider/FREEVIEW", "List of channels by provider: FREEVIEW"),
        Link("/channels/provider/TERRESTRIAL", "List of channels by provider: TERRESTRIAL"),
        Link("/channels/category/FILMS", "List of channels by category: FILMS"),
        Link("/channels/category/KID", "List of channels by category: KID"),
        Link("/channels/category/NEWS", "List of channels by category: NEWS"))
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
