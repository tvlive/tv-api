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

class TVRootControllerSpec extends PlaySpec with MustMatchers {


  "root" should {
    "return the list of tv links that help to start the navigation across the API" in new WithApplication() with RootSetup {
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
        Link("http://localhost:9000/channels", "List of all the channels"),
        Link("http://localhost:9000/providers", "List of all the channel TV providers"),
        Link("http://localhost:9000/categories", "List of all the channel TV categories"),
        Link("http://localhost:9000/channels/provider/CABLE", "List of channels by provider: CABLE"),
        Link("http://localhost:9000/channels/provider/FREEVIEW", "List of channels by provider: FREEVIEW"),
        Link("http://localhost:9000/channels/provider/TERRESTRIAL", "List of channels by provider: TERRESTRIAL"),
        Link("http://localhost:9000/channels/category/FILMS", "List of channels by category: FILMS"),
        Link("http://localhost:9000/channels/category/KID", "List of channels by category: KID"),
        Link("http://localhost:9000/channels/category/NEWS", "List of channels by category: NEWS"),
        Link("http://localhost:9000/tvcontent/film/CABLE/today", "FILMS today on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/film/CABLE/current", "FILMS now on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/film/CABLE/left", "FILMS left on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/series/CABLE/today", "SERIES today on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/series/CABLE/current", "SERIES now on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/series/CABLE/left", "SERIES left on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/program/CABLE/today", "PROGRAMS today on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/program/CABLE/current", "PROGRAMS now on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/program/CABLE/left", "PROGRAMS left on TV by provider: CABLE"),
        Link("http://localhost:9000/tvcontent/film/FREEVIEW/today", "FILMS today on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/film/FREEVIEW/current", "FILMS now on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/film/FREEVIEW/left", "FILMS left on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/series/FREEVIEW/today", "SERIES today on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/series/FREEVIEW/current", "SERIES now on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/series/FREEVIEW/left", "SERIES left on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/program/FREEVIEW/today", "PROGRAMS today on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/program/FREEVIEW/current", "PROGRAMS now on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/program/FREEVIEW/left", "PROGRAMS left on TV by provider: FREEVIEW"),
        Link("http://localhost:9000/tvcontent/film/TERRESTRIAL/today", "FILMS today on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/film/TERRESTRIAL/current", "FILMS now on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/film/TERRESTRIAL/left", "FILMS left on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/series/TERRESTRIAL/today", "SERIES today on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/series/TERRESTRIAL/current", "SERIES now on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/series/TERRESTRIAL/left", "SERIES left on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/program/TERRESTRIAL/today", "PROGRAMS today on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/program/TERRESTRIAL/current", "PROGRAMS now on TV by provider: TERRESTRIAL"),
        Link("http://localhost:9000/tvcontent/program/TERRESTRIAL/left", "PROGRAMS left on TV by provider: TERRESTRIAL")
    )

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
