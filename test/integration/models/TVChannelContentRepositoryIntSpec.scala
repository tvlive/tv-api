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

  implicit val time: TimeProvider = new TimeProvider {
    override def currentDate() = current
  }

  val tvContentRepository = new TVContentRepository(this.getClass.getCanonicalName)
  tvContentRepository.drop()
  Thread.sleep(5000)

  object Channel1 {
    val series1 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(4), current.minusHours(2),
      Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None, None)),
      None,
      None)

    val series2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(2), current,
      Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None, None)),
      None,
      None)

    val film1 = TVContent("channel1", List("FREEVIEW", "SKY"), current, current.plusHours(1),
      None,
      Some(Film("film1", List("actor5"), List(), List(), List(), List(), None, Some(7), None, None, None, None, None)),
      None)

    val film2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(1), current.plusHours(3),
      None,
      Some(Film("film2", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None, None)),
      None)

    val program1 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(3), current.plusHours(5),
      None,
      None,
      Some(Program("p5", Some("d5"))))

    val program2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(5), current.plusHours(7),
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }

  object Channel2 {
    val series3 = TVContent("channel2", List("FREEVIEW", "SKY"), current.minusHours(5), current,
      Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None, None)),
      None,
      None)

    val film3 = TVContent("channel2", List("FREEVIEW", "SKY"), current, current.plusHours(3),
      None,
      Some(Film("film3", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None, None)),
      None)

    val program3 = TVContent("channel2", List("FREEVIEW", "SKY"), current.plusHours(2), current.plusHours(7),
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }

  object Channel3 {
    val film4 = TVContent("channel3", List("FREEVIEW", "SKY"), current, current.plusHours(3),
      None,
      Some(Film("film4", List("actor1"), List(), List(), List(), List(), None, Some(8), None, None, None, None, None)),
      None)
  }


  object Channel4 {
    val program4 = TVContent("channel4", List("FREEVIEW", "SKY"), current.minusHours(1), current.plusHours(1),
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }


  before {
    import Channel1._
    import Channel2._
    import Channel3._
    import Channel4._
    whenReady(tvContentRepository.insertBulk(
      Enumerator(series1, series2, film1, film2, program1, program2, series3, film3, program3, film4, program4))) {
      response => response mustBe 11
    }
  }

  after {
    whenReady(tvContentRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
    }
  }


  "findDayContentByChannel" should {
    "return all the TV content for channel1 available today" in {
      whenReady(tvContentRepository.findDayContentByChannel("channel1")) {
        import Channel1._
        _ mustBe Seq(series1, series2, film1, film2, program1, program2)
      }
    }
  }

  "findCurrentContentByChannel" should {
    "return the TV content for channel1 available now" in {
      whenReady(tvContentRepository.findCurrentContentByChannel("channel1")) {
        import Channel1._
        _ mustBe Some(film1)
      }
    }
  }

  "findLeftContentByChannel" should {
    "return the TV content for channel 1 available from now until the end of the day" in {
      whenReady(tvContentRepository.findLeftContentByChannel("channel1")) {
        import Channel1._
        _ mustBe Seq(film1, film2, program1, program2)
      }
    }
  }

  "findContentByID" should {
    "return some TV Content for a particular ID" in {
      import Channel1._
      whenReady(tvContentRepository.findContentByID(series1.id.get.stringify)) {
        _.get mustBe series1
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
        _ mustBe Seq(Channel2.series3, Channel1.series1, Channel1.series2)
      }
    }

    "return all the TV content for a type film and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(Channel1.film1, Channel2.film3, Channel3.film4, Channel1.film2)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW" in {
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq(Channel4.program4, Channel2.program3, Channel1.program1, Channel1.program2)
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
        _ mustBe Seq(Channel3.film4, Channel1.film1, Channel2.film3)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW available now" in {
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq(Channel4.program4)
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
        _ mustBe Seq(Channel1.film1, Channel2.film3, Channel3.film4, Channel1.film2)
      }
    }

    "return all the TV content left for a type program and provider FREEVIEW" in {
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("program", "FREEVIEW")) {
        _ mustBe Seq(Channel4.program4, Channel2.program3, Channel1.program1, Channel1.program2)
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

  "findCurrentContentByProvider" should {
    "return all the TV content for provider FREEVIEW available now" in {
      whenReady(tvContentRepository.findCurrentContentByProvider("FREEVIEW")) {
        _ mustBe Seq(Channel3.film4, Channel1.film1, Channel4.program4, Channel2.film3)
      }
    }

    "return empty list of TV content for a provider SOME_PROVIDER does not exist available now" in {
      whenReady(tvContentRepository.findCurrentContentByProvider("SOME_PROVIDER")) {
        _ mustBe Seq()
      }
    }
  }
}

