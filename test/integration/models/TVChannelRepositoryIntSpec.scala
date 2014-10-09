package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator

class TVChannelRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures with MongoSugar {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelRepository = new TVChannelRepository(this.getClass.getCanonicalName)
  tvChannelRepository.drop()
  Thread.sleep(5000)

  val tvChannel1 = TVChannel("testTvChannel1", List("genre1"), List("cat1"))
  val tvChannel2 = TVChannel("testTvChannel2", List("ENTERTAINMENT"), List("cat2"))
  val tvChannel3 = TVChannel("testTvChannel3", List("genre1"), List("cat3"))
  val tvChannel4 = TVChannel("testTvChannel4", List("ENTERTAINMENT"), List("cat4"))

  before {
    whenReady(tvChannelRepository.insertBulk(Enumerator(tvChannel1, tvChannel2, tvChannel3, tvChannel4))) {
      response => response mustBe 4
    }
  }

  after {
    whenReady(tvChannelRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
    }
  }

  "listOfTVChannels" should {
    "return the list of TV Channels available today" in {
      whenReady(tvChannelRepository.listOfTVChannels()) {
        _ mustBe Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4)
      }
    }

    "return the list of TV Channels by genre ENTERTAINMENT" in {
      whenReady(tvChannelRepository.listOfTVChannelsByGenre("ENTERTAINMENT")) {
        _ mustBe Seq(tvChannel2, tvChannel4)
      }
    }
  }

}
