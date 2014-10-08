package models

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.DomainBuilder.TVShort
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

  val p1 = TVContent("channel1", current.minusHours(4), current.minusHours(2), Some(List("documentary", "ENTERTAINMENT")),
    Some(Series("serie1", "ep1", None, None, None, None, Some(List("actor1")))),
    Some(Film("program1", None, Some(List("actor2")), Some("2011"))),
    Some(Program("p1", Some("d1"))))

  val p2 = TVContent("channel1", current.minusHours(2), current, Some(List("documentary")),
    Some(Series("serie2", "ep1", None, None, None, None, Some(List("actor1")))),
    Some(Film("program1", None, Some(List("actor2")), Some("2013"))),
    Some(Program("p2", Some("d2"))))

  val p3 = TVContent("channel1", current, current.plusHours(1), Some(List("FILM", "ENTERTAINMENT")),
    Some(Series("serie3", "ep1", None, None, None, None, Some(List("actor3")))),
    Some(Film("program1", None, Some(List("actor5")), Some("1999"))),
    Some(Program("p3", Some("d3"))))

  val p4 = TVContent("channel1", current.plusHours(1), current.plusHours(3), Some(List("documentary")),
    Some(Series("serie4", "ep1", None, None, None, None, Some(List("actor1")))),
    Some(Film("program1", None, Some(List("actor1")), None)),
    Some(Program("p4", Some("d4"))))

  val p5 = TVContent("channel1", current.plusHours(3), current.plusHours(5), Some(List("documentary")),
    Some(Series("serie5", "ep1", None, None, None, None, Some(List("actor1")))),
    Some(Film("program1", None, Some(List("actor5")), Some("2011"))),
    Some(Program("p5", Some("d5"))))

  val p6 = TVContent("channel1", current.plusHours(5), current.plusHours(7), Some(List("documentary")),
    Some(Series("serie6", "ep1", None, None, None, None, Some(List("actor6")))),
    Some(Film("program1", None, Some(List("actor1")), None)),
    Some(Program("p6", Some("d6"))))


  before {
    whenReady(tvContentRepository.insertBulk(Enumerator(p1, p2, p3, p4, p5, p6))) {
      response => response mustBe 6
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
        _ mustBe Seq(TVShort(p1), TVShort(p2), TVShort(p3), TVShort(p4),
          TVShort(p5), TVShort(p6))
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
        _ mustBe Seq(TVShort(p3), TVShort(p4), TVShort(p5), TVShort(p6))
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

  "findDayContentByGenre" should {
    "return all the TV content for a genre ENTERTAINMENT" in {
      whenReady(tvContentRepository.findDayContentByGenre("ENTERTAINMENT")) {
        _ mustBe Seq(TVShort(p1), TVShort(p3))
      }
    }

    "return none TV Content for a genre FOOTBALL" in {
      whenReady(tvContentRepository.findDayContentByGenre("FOOTBALL")) {
        _ mustBe List()
      }
    }
  }

  "findCurrentContentByGenre" should {
    "return the TV content for a genre FILM available now" in {

      whenReady(tvContentRepository.findCurrentContentByGenre("FILM")) {
        _ mustBe Seq(TVShort(p3))
      }
    }

    "return none TV Content for a genre NEWS" in {
      whenReady(tvContentRepository.findCurrentContentByGenre("NEWS")) {
        _ mustBe List()
      }
    }
  }

  "findLeftContentByGenre" should {
    "return the TV content for a genre documentary available  from now until the end of the day" in {

      whenReady(tvContentRepository.findLeftContentByGenre("documentary")) {
        _ mustBe Seq(TVShort(p4), TVShort(p5), TVShort(p6))
      }
    }

    "return none TV Content for a genre NEWS from now until the end of the day" in {
      whenReady(tvContentRepository.findLeftContentByGenre("NEWS")) {
        _ mustBe List()
      }
    }
  }
}
