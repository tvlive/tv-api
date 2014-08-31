package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelCollectionName = this.getClass.getName
  val tvChannelRepository = TVChannelRepository(tvChannelCollectionName)
  val collection = tvChannelRepository.collection

  val tvChannel1 = TVChannel("testTvChannel1", "EN")
  val tvChannel2 = TVChannel("testTvChannel2", "EN")
  val tvChannel3 = TVChannel("testTvChannel3", "EN")
  val tvChannel4 = TVChannel("testTvChannel4", "EN")

  before {
    whenReady(collection.bulkInsert(Enumerator(tvChannel1, tvChannel2, tvChannel3, tvChannel4))) {
      response => response mustBe 4
    }
  }

  after {
    whenReady(collection.drop()) {
      response => println(s"Collection $tvChannelCollectionName has been drop: $response")
    }
  }

  "listOfTVChannels" should {
    "return the list of TV Channels available today" in {
      val allTVChannels = tvChannelRepository.listOfTVChannels()
      val channels = Await.result(allTVChannels, Duration("20 seconds"))

      channels mustBe Seq( tvChannel1, tvChannel2, tvChannel3, tvChannel4)
    }
  }

}
