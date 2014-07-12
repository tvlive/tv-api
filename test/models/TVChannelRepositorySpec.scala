package models

import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter {

  val tvChannelCollectionName = this.getClass.getName
  val tvChannelRepository = TVChannelRepository(tvChannelCollectionName)
  val collection = tvChannelRepository.collection

  val c1Id = Some(BSONObjectID.generate)
  val c2Id = Some(BSONObjectID.generate)
  val c3Id = Some(BSONObjectID.generate)
  val c4Id = Some(BSONObjectID.generate)

  before {

    val channel1 = collection.insert[TVChannel](TVChannel(c1Id,"testTvChannel1", "EN"))
    val channel2 = collection.insert[TVChannel](TVChannel(c2Id,"testTvChannel2", "EN"))
    val channel3 = collection.insert[TVChannel](TVChannel(c3Id,"testTvChannel3", "EN"))
    val channel4 = collection.insert[TVChannel](TVChannel(c4Id,"testTvChannel4", "EN"))

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

      channels mustBe Seq(
        TVChannel(c1Id,"testTvChannel1", "EN"),
        TVChannel(c2Id,"testTvChannel2", "EN"),
        TVChannel(c3Id,"testTvChannel3", "EN"),
        TVChannel(c4Id,"testTvChannel4", "EN")
      )
    }
  }

}
