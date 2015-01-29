package models

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider

class TVChannelContentRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures with MongoSugar {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val current = new DateTime(2010, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))

  trait FakeTimeProvider extends TimeProvider {
    override def currentDate() = current
  }

  val tvContentRepository = new TVContentRepository(this.getClass.getCanonicalName) with FakeTimeProvider
  tvContentRepository.drop()
  Thread.sleep(5000)

  val p1 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(4), current.minusHours(2), List("documentary", "ENTERTAINMENT"),
    Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
      List(), List(), List(), None, None, None, None, None, None, None, None)),
    None,
    None)

  val p2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(2), current, List("documentary"),
    Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
      List(), List(), List(), None, None, None, None, None, None, None, None)),
    None,
    None)

  val p7 = TVContent("channel2", List("FREEVIEW", "SKY"), current.minusHours(5), current, List("documentary"),
    Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
      List(), List(), List(), None, None, None, None, None, None, None, None)),
    None,
    None)

  val p3 = TVContent("channel1", List("FREEVIEW", "SKY"), current, current.plusHours(1), List("FILM", "ENTERTAINMENT"),
    None,
    Some(Film("program1", List("actor5"),List(), List(), List(), None, None, None, None, None, None, None, None)),
    None)

  val p4 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(1), current.plusHours(3), List("documentary"),
    None,
    Some(Film("program1", List("actor1"), List(), List(), List(), None, None, None, None, None, None, None, None)),
    None)

  val p8 = TVContent("channel2", List("FREEVIEW", "SKY"), current, current.plusHours(3), List("documentary"),
    None,
    Some(Film("program1", List("actor1"), List(), List(), List(), None, None, None, None, None, None, None, None)),
    None)

  val p5 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(3), current.plusHours(5), List("documentary"),
    None,
    None,
    Some(Program("p5", Some("d5"))))

  val p6 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(5), current.plusHours(7), List("documentary"),
    None,
    None,
    Some(Program("p6", Some("d6"))))

  val p9 = TVContent("channel2", List("FREEVIEW", "SKY"), current.plusHours(2), current.plusHours(7), List("documentary"),
    None,
    None,
    Some(Program("p6", Some("d6"))))


  before {
    whenReady(tvContentRepository.insertBulk(Enumerator(p1, p2, p3, p4, p5, p6, p7, p8, p9))) {
      response => response mustBe 9
    }
  }

  after {
    whenReady(tvContentRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
    }
  }

  "findDayContentByChannel" should {
    "return all the TV content for a particular channel available today" in {
      whenReady(tvContentRepository.findDayContentByChannel("channel1")) {
        _ mustBe Seq(p1, p2, p3, p4, p5, p6)
      }
    }
  }

  "findCurrentContentByChannel" should {
    "return the TV content for a particular channel available now" in {

      whenReady(tvContentRepository.findCurrentContentByChannel("channel1")) {
        _ mustBe Some(p3)
      }
    }
  }

  "findLeftContentByChannel" should {
    "return the TV content for a particular channel available from now until the end of the day" in {
      whenReady(tvContentRepository.findLeftContentByChannel("channel1")) {
        _ mustBe Seq(p3, p4, p5, p6)
      }
    }
  }

  "findContentByID" should {
    "return some TV Content for a particular ID" in {
      whenReady(tvContentRepository.findContentByID(p1.id.get.stringify)) {
        _.get mustBe p1
      }
    }

    "return none TV Content for a particular ID" in {
      whenReady(tvContentRepository.findContentByID(BSONObjectID.generate.stringify)) {
        _ mustBe None
      }
    }
  }

  "findDayContentByTypeAndProvider" should {
    "return all the TV content for a type series and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("series", "FREEVIEW")) {
        _ mustBe Seq(p7, p1, p2)
      }
    }

    "return all the TV content for a type film and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(p3, p8, p4)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq(p9, p5, p6)
      }
    }

    "return empty list of TV content for a type nonExist and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("nonExist", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return empty list of TV content for a type series and provider nonExist" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("series", "nonExist")) {
        _ mustBe Seq()
      }
    }
  }


  "findCurrentContentByTypeAndProvider" should {
    "return all the TV content for a type series and provider FREEVIEW available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("series", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return all the TV content for a type film and provider FREEVIEW  available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(p3, p8)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return empty list of TV content for a type nonExist and provider FREEVIEW  available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("nonExist", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return empty list of TV content for a type series and provider nonExist available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("film", "nonExist")) {
        _ mustBe Seq()
      }
    }
  }

  "findLeftContentByTypeAndProvider" should {
    "return all the TV content left for a type series and provider FREEVIEW" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("series", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return all the TV content left for a type film and provider FREEVIEW" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(p3, p8, p4)
      }
    }

    "return all the TV content left for a type program and provider FREEVIEW" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq(p9, p5, p6)
      }
    }

    "return empty list of TV content left for a type nonExist and provider FREEVIEW" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("nonExist", "FREEVIEW")) {
        _ mustBe Seq()
      }
    }

    "return empty list of TV content left for a type series and provider nonExist" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("program", "nonExist")) {
        _ mustBe Seq()
      }
    }
  }
}
