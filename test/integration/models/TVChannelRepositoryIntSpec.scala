package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator

class TVChannelRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfterAll with ScalaFutures with MongoSugar {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(3, Seconds), interval = Span(5, Millis))

  val tvChannelRepository = new TVChannelRepository(this.getClass.getCanonicalName)

  val tvChannel1 = TVChannel("testTvChannel1", List("PROVIDER1"), List("category1"))
  val tvChannel2 = TVChannel("testTvChannel2", List("PROVIDER2"), List("ENTERTAINMENT"))
  val tvChannel3 = TVChannel("testTvChannel3", List("PROVIDER3"), List("category1"))
  val tvChannel4 = TVChannel("testTvChannel4", List("PROVIDER3"), List("ENTERTAINMENT"))

  override def beforeAll {
    whenReady(tvChannelRepository.removeAll()){
      ok => println(s"Before - collection ${this.getClass.getCanonicalName} removed: $ok")
    }
    whenReady(tvChannelRepository.insertBulk(Enumerator(tvChannel1, tvChannel2, tvChannel3, tvChannel4))) {
      response => response mustBe 4
    }
  }

  override def afterAll {
    whenReady(tvChannelRepository.drop){
      ok => println(s"After - collection ${this.getClass.getCanonicalName} dropped: $ok")
    }
  }

  "listOfTVChannels" should {
    "return the list of TV Channels available today" in {
      whenReady(tvChannelRepository.listOfTVChannels()) {
        _ mustBe Seq(tvChannel1, tvChannel2, tvChannel3, tvChannel4)
      }
    }

    "return the list of TV Channels by category ENTERTAINMENT" in {
      whenReady(tvChannelRepository.listOfTVChannelsByCategory("ENTERTAINMENT")) {
        _ mustBe Seq(tvChannel2, tvChannel4)
      }
    }

    "return empty list of TV Channels by category NONEXIST" in {
      whenReady(tvChannelRepository.listOfTVChannelsByCategory("NONEXIST")) {
        _ mustBe Seq()
      }
    }

    "return the list of TV Channels by provider PROVIDER3" in {
      whenReady(tvChannelRepository.listOfTVChannelsByProvider("PROVIDER3")) {
        _ mustBe Seq(tvChannel3, tvChannel4)
      }
    }

    "return empty list of TV Channels by provider NONEXIST" in {
      whenReady(tvChannelRepository.listOfTVChannelsByProvider("NONEXIST")) {
        _ mustBe Seq()
      }
    }
  }

}
