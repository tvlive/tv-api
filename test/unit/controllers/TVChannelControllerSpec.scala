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

    "provide the list of channels for that contains genre ENTERTAINMENT in upper case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByCategory("ENTERTAINMENT").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel2, tvChannel4)
    }

    "provide the list of channels for that contains genre documentary in lower case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByCategory("documentary").apply(FakeRequest())
      status(channelResult) mustBe(OK)
      contentType(channelResult) mustBe(Some("application/json"))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1, tvChannel3)
    }
  }
}

trait TVChannelSetUpTest {

  val tvChannel1 = TVChannel("testTvChannel1", List("DOCUMENTARY"), List("cat1"), Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", List("ENTERTAINMENT"), List("cat2"), Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", List("DOCUMENTARY"), List("cat3"), Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", List("ENTERTAINMENT"), List("cat4"), Some(BSONObjectID.generate))

  val tvChannelRepository = new ChannelRepository {
    override def listOfTVChannels(): Future[Seq[TVChannel]] = {
      Future.successful(Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4))
    }

    override def listOfTVChannelsByCategory(genre: String): Future[Seq[TVChannel]] = {
      genre match {
        case "ENTERTAINMENT" => Future.successful(Seq(tvChannel2, tvChannel4))
        case "DOCUMENTARY" => Future.successful(Seq(tvChannel1, tvChannel3))
      }
    }
  }

  class App extends controllers.TVChannelController {
    override val channelRepository: ChannelRepository = tvChannelRepository
  }

  val controller = new App

}
