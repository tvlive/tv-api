package controllers

import models.{ChannelCategoryRepository, ChannelProviderRepository, TVChannelCategory, TVChannelProvider}
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TVRootControllerSpec extends PlaySpec with MustMatchers  {


  "root" should {
    "return the list of tv links that help to start the navigation across the API" in  new WithApplication() with RootSetup  {
      //GIVEN
      when(providerRepo.findAll()).thenReturn(Future(Seq(tvChannelProvider3, tvChannelProvider1, tvChannelProvider2)))
      when(categoryRepo.findAll()).thenReturn(Future(Seq(tvChannelCategory1, tvChannelCategory2, tvChannelCategory3)))

     //WHEN
      val rootsResponse: Future[SimpleResult] = rootController.roots.apply(FakeRequest())

      //THEN
      status(rootsResponse) mustBe (OK)
      contentType(rootsResponse) mustBe (Some("application/json"))
      val linksInResponse = contentAsString(rootsResponse)
      val links = Json.parse(linksInResponse).as[Seq[Link]]
      links mustEqual Seq(
        Link("/channels", "List of all the channels"),
        Link("/providers", "List of all the channel TV providers"),
        Link("/categories", "List of all the channel TV categories"),
        Link("/channels/provider/CABLE", "List of channels by provider: CABLE"),
        Link("/channels/provider/FREEVIEW", "List of channels by provider: FREEVIEW"),
        Link("/channels/provider/TERRESTRIAL", "List of channels by provider: TERRESTRIAL"),
        Link("/channels/category/FILMS", "List of channels by category: FILMS"),
        Link("/channels/category/KID", "List of channels by category: KID"),
        Link("/channels/category/NEWS", "List of channels by category: NEWS"))

      verify(providerRepo).findAll()
      verify(categoryRepo).findAll()
    }
  }
}

trait RootSetup extends MockitoSugar {
  val tvChannelProvider1 = TVChannelProvider("FREEVIEW")
  val tvChannelProvider2 = TVChannelProvider("TERRESTRIAL")
  val tvChannelProvider3 = TVChannelProvider("CABLE")

  val providerRepo = mock[ChannelProviderRepository]

  val tvChannelCategory1 = TVChannelCategory("FILMS")
  val tvChannelCategory2 = TVChannelCategory("KID")
  val tvChannelCategory3 = TVChannelCategory("NEWS")

  val categoryRepo = mock[ChannelCategoryRepository]

  class App extends TVRootController {
    override val providerRepository: ChannelProviderRepository = providerRepo
    override val categoryRepository: ChannelCategoryRepository = categoryRepo
  }

  val rootController = new App

}
