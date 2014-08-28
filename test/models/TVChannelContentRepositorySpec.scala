package models

import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class TVChannelContentRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter {

  val current = new DateTime(2010,10,10,10,0,0)
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
    Thread.sleep(5000)
  }

  "findDayContentByChannel" should {
    "return all the TV content for a particular channel available today" in {
      val content = tvContentRepository.findDayContentByChannel("channel1")
      val result = Await.result(content, Duration("10 seconds"))
      result mustBe Seq(
        TVProgramShort(p1.channel, p1.startTime, p1.endTime, p1.category, p1.id),
        TVProgramShort(p2.channel, p2.startTime, p2.endTime, p2.category, p2.id),
        TVProgramShort(p3.channel, p3.startTime, p3.endTime, p3.category, p3.id),
        TVProgramShort(p4.channel, p4.startTime, p4.endTime, p4.category, p4.id),
        TVProgramShort(p5.channel, p5.startTime, p5.endTime, p5.category, p5.id),
        TVProgramShort(p6.channel, p6.startTime, p6.endTime, p6.category, p6.id)
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
      result mustBe Seq(
        TVProgramShort(p3.channel, p3.startTime, p3.endTime, p3.category, p3.id),
        TVProgramShort(p4.channel, p4.startTime, p4.endTime, p4.category, p4.id),
        TVProgramShort(p5.channel, p5.startTime, p5.endTime, p5.category, p5.id),
        TVProgramShort(p6.channel, p6.startTime, p6.endTime, p6.category, p6.id))
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
