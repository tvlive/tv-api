package models

import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import utils.TimeProvider

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelContentRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter {

  trait FakeTimeProvider extends TimeProvider {
    override def currentDate() = 3
  }

  val collectionName = this.getClass.getName
  val tvContentRepository = new TVContentRepository(collectionName) with FakeTimeProvider
  val collection = tvContentRepository.collection

  val p1 = TVProgram("channel1", "programName1", 0, 1, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))
  val p2 = TVProgram("channel1", "programName2", 1, 2, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))
  val p3 = TVProgram("channel1", "programName3", 3, 4, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))
  val p4 = TVProgram("channel1", "programName4", 4, 5, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))
  val p5 = TVProgram("channel1", "programName5", 5, 6, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))
  val p6 = TVProgram("channel1", "programName6", 6, 7, Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Program("program1", None))


  before {
    val content1 = collection.insert(p1)
    val content2 = collection.insert(p2)
    val content3 = collection.insert(p3)
    val content4 = collection.insert(p4)
    val content5 = collection.insert(p5)
    val content6 = collection.insert(p6)

    val result = for {
      c1 <- content1
      c2 <- content2
      c3 <- content3
      c4 <- content4
      c5 <- content5
      c6 <- content6
    } yield c1.ok && c2.ok && c3.ok && c4.ok && c5.ok && c6.ok

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

  "findDayContentByChannel" should {
    "return all the TV content for a particular channel available today" in {
      val content = tvContentRepository.findDayContentByChannel("channel1")
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe Seq(p1, p2, p3, p4, p5, p6)
    }
  }

  "findCurrentContentByChannel" should {
    "return the TV content for a particular channel available now" in {

      val content = tvContentRepository.findCurrentContentByChannel("channel1")
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe Some(p3)
    }
  }

  "findLeftContentByChannel" should {
    "return the TV content for a particular channel available from now until the end of the day" in {
      val content = tvContentRepository.findLeftContentByChannel("channel1")
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe Seq(p3, p4, p5, p6)
    }
  }


}
