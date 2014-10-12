package controllers

import models.{ChannelRepository, TVChannel}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

class TVChannelControllerSpec extends PlaySpec with MustMatchers with TVChannelSetUpTest {


  "TVChannelController" should {

    "provide the list of channels available in TV" in {

      val channelResult: Future[SimpleResult] = controller.channels().apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4)
    }

    "provide the list of channels for that contains category ENTERTAINMENT in upper case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByCategory("ENTERTAINMENT").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel2, tvChannel4)
    }

    "provide the list of channels for that contains category documentary in lower case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByCategory("documentary").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1, tvChannel3)
    }

    "provide NOT_FOUND if category does not exist" in {

      val channelResult: Future[SimpleResult] = controller.channelsByCategory("NOEXIST").apply(FakeRequest())
      status(channelResult) mustBe(NOT_FOUND)
    }

    "provide the list of channels for that contains provider PROVIDER2 in upper case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByProvider("PROVIDER2").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel2, tvChannel4)
    }

    "provide the list of channels for that contains provider provider1 in lower case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByProvider("provider1").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1)
    }

    "provide NOT_FOUND if provider does not exist" in {

      val channelResult: Future[SimpleResult] = controller.channelsByProvider("NOEXIST").apply(FakeRequest())
      status(channelResult) mustBe(NOT_FOUND)
    }
  }
}

trait TVChannelSetUpTest {

  val tvChannel1 = TVChannel("testTvChannel1", List("PROVIDER1"), List("DOCUMENTARY"), Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", List("PROVIDER2"), List("ENTERTAINMENT"), Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", List("PROVIDER3"),List("DOCUMENTARY"),  Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", List("PROVIDER2"), List("ENTERTAINMENT"), Some(BSONObjectID.generate))

  val tvChannelRepository = new ChannelRepository {
    override def listOfTVChannels(): Future[Seq[TVChannel]] = {
      Future.successful(Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4))
    }

    override def listOfTVChannelsByCategory(category: String): Future[Seq[TVChannel]] = {
      category match {
        case "ENTERTAINMENT" => Future.successful(Seq(tvChannel2, tvChannel4))
        case "DOCUMENTARY" => Future.successful(Seq(tvChannel1, tvChannel3))
        case _ => Future.successful(Seq())
      }
    }

    override def listOfTVChannelsByProvider(provider: String): Future[Seq[TVChannel]] =
     provider match {
       case "PROVIDER1" => Future.successful(Seq(tvChannel1))
       case "PROVIDER2" => Future.successful(Seq(tvChannel2, tvChannel4))
       case "PROVIDER3" => Future.successful(Seq(tvChannel3))
       case _ => Future.successful(Seq())
     }

  }

  class App extends controllers.TVChannelController {
    override val channelRepository: ChannelRepository = tvChannelRepository
  }

  val controller = new App

}
