package models

import org.joda.time.DateTime
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelContentRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val current = new DateTime(2010, 10, 10, 10, 0, 0)

  trait FakeTimeProvider extends TimeProvider {
    override def currentDate() = current
  }

  val collectionName = this.getClass.getName
  val tvContentRepository = new TVContentRepository(collectionName) with FakeTimeProvider
  val collection = tvContentRepository.collection

  val p1 = TVProgram("channel1", current.minusHours(4), current.minusHours(2), Some("documentary"), Some("flags1"), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)))
  val p2 = TVProgram("channel1", current.minusHours(2), current, Some("documentary"), Some("flags1"), Some(Serie("serie2", "ep1", None, None, None, None)), Some(Program("program1", None)))
  val p3 = TVProgram("channel1", current, current.plusHours(1), Some("documentary"), Some("flags1"), Some(Serie("serie3", "ep1", None, None, None, None)), Some(Program("program1", None)))
  val p4 = TVProgram("channel1", current.plusHours(1), current.plusHours(3), Some("documentary"), Some("flags1"), Some(Serie("serie4", "ep1", None, None, None, None)), Some(Program("program1", None)))
  val p5 = TVProgram("channel1", current.plusHours(3), current.plusHours(5), Some("documentary"), Some("flags1"), Some(Serie("serie5", "ep1", None, None, None, None)), Some(Program("program1", None)))
  val p6 = TVProgram("channel1", current.plusHours(5), current.plusHours(7), Some("documentary"), Some("flags1"), Some(Serie("serie6", "ep1", None, None, None, None)), Some(Program("program1", None)))


  before {
    whenReady(collection.bulkInsert(Enumerator(p1, p2, p3, p4, p5, p6))) {
      response => response mustBe 6
    }
  }

  after {
    whenReady(collection.drop()) {
      response => println(s"Collection $collectionName has been drop: $response")
    }
  }

  "findDayContentByChannel" should {
    "return all the TV content for a particular channel available today" in {
      val content = tvContentRepository.findDayContentByChannel("channel1")
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe Seq(TVShort(p1), TVShort(p2), TVShort(p3), TVShort(p4),
        TVShort(p5), TVShort(p6)
      )

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
      result mustBe Seq(TVShort(p3), TVShort(p4), TVShort(p5), TVShort(p6))
    }
  }

  "findContentByID" should {
    "return some TV Content for a particular ID" in {
      val content = tvContentRepository.findContentByID(p1.id.get.stringify)
      val result = Await.result(content, Duration("10 seconds"))
      result.get mustBe p1
    }

    "return none TV Content for a particular ID" in {
      val content = tvContentRepository.findContentByID(BSONObjectID.generate.stringify)
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe None
    }
  }
}
