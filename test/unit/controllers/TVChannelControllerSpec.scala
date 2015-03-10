package controllers

import controllers.external.{ChannelLong, TVChannelLong}
import models.{ChannelRepository, TVChannel}
import org.mockito.Mockito._
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

class TVChannelControllerSpec extends PlaySpec with MustMatchers {

  "TVChannelController" should {

    "provide the list of channels available in TV" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannels()).thenReturn(Future.successful(Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4)))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channels().apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannelLong]]
      channelsInResponse mustEqual Seq(ChannelLong(tvChannel1), ChannelLong(tvChannel2), ChannelLong(tvChannel3), ChannelLong(tvChannel4))

      //AND
      verify(tvChannelRepository).listOfTVChannels()
    }

    "provide the list of channels for that contains category ENTERTAINMENT in upper case" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByCategory("ENTERTAINMENT")).thenReturn(Future.successful(Seq(tvChannel2, tvChannel4)))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByCategory("ENTERTAINMENT").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannelLong]]
      channelsInResponse mustEqual Seq(ChannelLong(tvChannel2), ChannelLong(tvChannel4))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByCategory("ENTERTAINMENT")
    }

    "provide the list of channels for that contains category documentary in lower case" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByCategory("DOCUMENTARY")).thenReturn(Future.successful(Seq(tvChannel1, tvChannel3)))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByCategory("documentary").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannelLong]]
      channelsInResponse mustEqual Seq(ChannelLong(tvChannel1), ChannelLong(tvChannel3))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByCategory("DOCUMENTARY")
    }

    "provide NOT_FOUND if category does not exist" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByCategory("NOEXIST")).thenReturn(Future.successful(Seq()))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByCategory("NOEXIST").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(NOT_FOUND)
      val channelsInResponse = contentAsJson(channelResult).as[NotFoundResponse]
      channelsInResponse mustEqual(NotFoundResponse(s"No channels found for the category: NOEXIST"))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByCategory("NOEXIST")
    }

    "provide the list of channels for that contains provider PROVIDER2 in upper case" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByProvider("PROVIDER2")).thenReturn(Future.successful(Seq(tvChannel2, tvChannel4)))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByProvider("PROVIDER2").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannelLong]]
      channelsInResponse mustEqual Seq(ChannelLong(tvChannel2), ChannelLong(tvChannel4))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByProvider("PROVIDER2")
    }

    "provide the list of channels for that contains provider provider1 in lower case" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByProvider("PROVIDER1")).thenReturn(Future.successful(Seq(tvChannel1)))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByProvider("provider1").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannelLong]]
      channelsInResponse mustEqual Seq(ChannelLong(tvChannel1))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByProvider("PROVIDER1")
    }

    "provide NOT_FOUND if provider does not exist" in new TVChannelSetUpTest() {
      //GIVEN
      when(tvChannelRepository.listOfTVChannelsByProvider("NOEXIST")).thenReturn(Future.successful(Seq()))

      //WHEN
      val channelResult: Future[SimpleResult] = controller.channelsByProvider("NOEXIST").apply(FakeRequest())

      //THEN
      status(channelResult) mustBe(NOT_FOUND)
      val channelsInResponse = contentAsJson(channelResult).as[NotFoundResponse]
      channelsInResponse mustEqual(NotFoundResponse(s"No channels found for the provider: NOEXIST"))

      //AND
      verify(tvChannelRepository).listOfTVChannelsByProvider("NOEXIST")
    }
  }
}

trait TVChannelSetUpTest extends MockitoSugar {

  val tvChannel1 = TVChannel("testTvChannel1", List("PROVIDER1"), List("DOCUMENTARY"), Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", List("PROVIDER2"), List("ENTERTAINMENT"), Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", List("PROVIDER3"),List("DOCUMENTARY"),  Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", List("PROVIDER2"), List("ENTERTAINMENT"), Some(BSONObjectID.generate))

  val tvChannelRepository = mock[ChannelRepository]

  class App extends controllers.TVChannelController {
    override val channelRepository: ChannelRepository = tvChannelRepository
  }

  val controller = new App

}
