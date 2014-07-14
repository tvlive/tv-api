package models

import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter {

  val tvChannelCollectionName = this.getClass.getName
  val tvChannelRepository = TVChannelRepository(tvChannelCollectionName)
  val collection = tvChannelRepository.collection

  val tvChannel1 = TVChannel("testTvChannel1", "EN")
  val tvChannel2 = TVChannel("testTvChannel2", "EN")
  val tvChannel3 = TVChannel("testTvChannel3", "EN")
  val tvChannel4 = TVChannel("testTvChannel4", "EN")
  before {

    val channel1 = collection.insert[TVChannel](tvChannel1)
    val channel2 = collection.insert[TVChannel](tvChannel2)
    val channel3 = collection.insert[TVChannel](tvChannel3)
    val channel4 = collection.insert[TVChannel](tvChannel4)

    val result = for {
      c1 <- channel1
      c2 <- channel1
      c3 <- channel1
      c4 <- channel1
    } yield (c1.ok && c2.ok && c3.ok && c4.ok)

    val isOk = Await.result(result, Duration("20 seconds"))

    isOk match {
      case true => println("Elements inserted")
      case false => {
        collection.drop()
        throw new Exception("Error inserting elements")
      }
    }
  }

  after {
    collection.drop()
  }

  "listOfTVChannels" should {
    "return the list of TV Channels available today" in {
      val allTVChannels = tvChannelRepository.listOfTVChannels()
      val channels = Await.result(allTVChannels, Duration("20 seconds"))

      channels mustBe Seq( tvChannel1, tvChannel2, tvChannel3, tvChannel4)
    }
  }

}
