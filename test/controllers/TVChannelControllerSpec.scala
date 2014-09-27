package controllers

import models.{ChannelRepository, TVChannel}
import org.junit.runner._
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class TVChannelControllerSpec extends Specification with TVChannelSetUpTest {


  "TVChannelController" should {

    //    "send 404 on a bad request" in new WithApplication {
    //      route(FakeRequest(GET, "/boum")) must beNone
    //    }
    //
    //    "render the index page" in new WithApplication {
    //      val home = route(FakeRequest(GET, "/")).get
    //
    //      status(home) must equalTo(OK)
    //      contentType(home) must beSome.which(_ == "text/html")
    //      contentAsString(home) must contain("Your new application is ready.")
    //    }

    //    "provide empty list of channels available in TV" in new WithApplication {
    //
    //      val tvChannelRepository = TVChannelRepository("tvChannel")
    //      val collection = tvChannelRepository.collection
    //      collection.drop()
    //
    //      val channels = route(FakeRequest(GET, "/channels")).get
    //      status(channels) must equalTo(NOT_FOUND)
    //
    //    }

    "provide the list of channels available in TV" in {

      val channelResult: Future[SimpleResult] = controller.channels().apply(FakeRequest())
      status(channelResult) must equalTo(OK)
      contentType(channelResult) must beSome.which(_ == "application/json")
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4)
    }

    "provide the list of channels for that contains genre ENTERTAINMENT in upper case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByGenre("ENTERTAINMENT").apply(FakeRequest())
      status(channelResult) must equalTo(OK)
      contentType(channelResult) must beSome.which(_ == "application/json")
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel2, tvChannel4)
    }

    "provide the list of channels for that contains genre documentary in lower case" in {

      val channelResult: Future[SimpleResult] = controller.channelsByGenre("documentary").apply(FakeRequest())
      status(channelResult) must equalTo(OK)
      contentType(channelResult) must beSome.which(_ == "application/json")
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse mustEqual Seq(tvChannel1, tvChannel3)
    }
  }
}

trait TVChannelSetUpTest {

  self: Specification =>

  val tvChannel1 = TVChannel("testTvChannel1", "DOCUMENTARY", "EN", Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", "ENTERTAINMENT", "EN", Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", "DOCUMENTARY", "EN", Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", "ENTERTAINMENT", "EN", Some(BSONObjectID.generate))

  val tvChannelRepository = new ChannelRepository {
    override def listOfTVChannels(): Future[Seq[TVChannel]] = {
      Future.successful(Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4))
    }

    override def listOfTVChannelsByGenre(genre: String): Future[Seq[TVChannel]] = {
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
