package controllers

import controllers.external.{TVChannelProviderExternal, ChannelProviderExternal}
import models.{ChannelProviderRepository, TVChannelProvider}
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TVChannelProviderControllerSpec extends PlaySpec with MustMatchers {

  "TVChannelProviderController" should {

    "provide the all the list of providers availables for tv channels order alphabetically" in new TVChannelProviderSetUpTest() {
      //GIVEN
      when(tvChannelProviderRepository.findAll()).thenReturn(Future(Seq(tvChannelProvider3, tvChannelProvider4, tvChannelProvider2, tvChannelProvider1)))

      //WHEN
      val providersResult: Future[SimpleResult] = controller.providers().apply(FakeRequest())

      //THEN
      status(providersResult) mustBe(OK)
      contentType(providersResult) mustBe(Some("application/json"))
      val genresInResponse = contentAsJson(providersResult).as[Seq[TVChannelProviderExternal]]
      genresInResponse mustEqual Seq(channelProvider3, channelProvider4, channelProvider2, channelProvider1)
    }
  }
}


trait TVChannelProviderSetUpTest extends MockitoSugar {

  val tvChannelProvider1 = TVChannelProvider("TERRESTRIAL", Some(BSONObjectID.generate))
  val tvChannelProvider2 = TVChannelProvider("SKY", Some(BSONObjectID.generate))
  val tvChannelProvider3 = TVChannelProvider("FREEVIEW", Some(BSONObjectID.generate))
  val tvChannelProvider4 = TVChannelProvider("PROVIDER", Some(BSONObjectID.generate))

  val channelProvider1 = ChannelProviderExternal(tvChannelProvider1)
  val channelProvider2 = ChannelProviderExternal(tvChannelProvider2)
  val channelProvider3 = ChannelProviderExternal(tvChannelProvider3)
  val channelProvider4 = ChannelProviderExternal(tvChannelProvider4)

  val tvChannelProviderRepository = mock[ChannelProviderRepository]

  class App extends controllers.TVChannelProviderController {
    override val channelProviderReporitory = tvChannelProviderRepository
  }

  val controller = new App

}
