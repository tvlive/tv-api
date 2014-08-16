package controllers

import models.{TVChannel, TVChannelRepository}
import org.junit.runner._
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnitRunner])
class TVChannelControllerTest extends Specification with TVChannelSetUpTest {


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
      println(contentAsString(channelResult))
      val channelsInResponse = contentAsJson(channelResult).as[Seq[TVChannel]]
      channelsInResponse must contain(tvChannel1)
      channelsInResponse must contain(tvChannel2)
      channelsInResponse must contain(tvChannel3)
      channelsInResponse must contain(tvChannel4)
    }
  }

}
trait TVChannelSetUpTest {

  val tvChannelRepository = TVChannelRepository("tvChannelTest")
  val channels = tvChannelRepository.collection
  channels.drop()
  Thread.sleep(5000)

  val tvChannel1 = TVChannel("testTvChannel1", "EN", Some(BSONObjectID.generate))
  val tvChannel2 = TVChannel("testTvChannel2", "EN", Some(BSONObjectID.generate))
  val tvChannel3 = TVChannel("testTvChannel3", "EN", Some(BSONObjectID.generate))
  val tvChannel4 = TVChannel("testTvChannel4", "EN", Some(BSONObjectID.generate))

  val channel1 = channels.insert[TVChannel](tvChannel1)
  val channel2 = channels.insert[TVChannel](tvChannel2)
  val channel3 = channels.insert[TVChannel](tvChannel3)
  val channel4 = channels.insert[TVChannel](tvChannel4)

  val resultChannels = for {
    c1 <- channel1
    c2 <- channel2
    c3 <- channel3
    c4 <- channel4
  } yield (c1.ok && c2.ok && c3.ok && c4.ok)

  val isOkChannels = Await.result(resultChannels, Duration("20 seconds"))

  isOkChannels match {
    case true => println("Elements inserted")
    case false => {
      channels.drop()
      throw new Exception("Error inserting elements")
    }
  }

  class App extends controllers.TVChannelController {
    override val channelRepository: TVChannelRepository = tvChannelRepository
  }

  val controller = new App

}
