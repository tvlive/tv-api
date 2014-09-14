package controllers

import models.{TVChannel, TVChannelRepository}
import org.junit.runner._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.libs.iteratee.Enumerator
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
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
trait TVChannelSetUpTest extends ScalaFutures {

  self:Specification =>

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelRepository = TVChannelRepository("tvChannelTest")
  val channels = tvChannelRepository.collection
  channels.drop()
  Thread.sleep(5000)

  val tvChannel1 = TVChannel("testTvChannel1", "DOCUMENTARY", "EN", Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", "ENTERTAINMENT", "EN", Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", "DOCUMENTARY", "EN", Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", "ENTERTAINMENT", "EN", Some(BSONObjectID.generate))

  whenReady(channels.bulkInsert(Enumerator(tvChannel1, tvChannel2, tvChannel3, tvChannel4))) {
    response => response must_== 4
  }


  class App extends controllers.TVChannelController {
    override val channelRepository: TVChannelRepository = tvChannelRepository
  }

  val controller = new App

}
