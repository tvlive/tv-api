package models

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider

class TVChannelContentRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfterAll with ScalaFutures with MongoSugar {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(3, Seconds), interval = Span(5, Millis))

  val current = new DateTime(2010, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))

  implicit val time: TimeProvider = new TimeProvider {
    override def currentDate() = current
  }

  val tvContentRepository = new TVContentRepository(this.getClass.getCanonicalName)
  whenReady(tvContentRepository.createIndex()) {
    ok => println(s"index created correctly collection ${this.getClass.getCanonicalName} $ok")
  }


  object Channel1 {
    val series1 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(4), current.minusHours(2), None,
      Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val series2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.minusHours(2), current, None,
      Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val film1 = TVContent("channel1", List("FREEVIEW", "SKY"), current, current.plusHours(1), Some(7),
      None,
      Some(Film("film1", List("actor5"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val film2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(1), current.plusHours(3), None,
      None,
      Some(Film("film2", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val program1 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(3), current.plusHours(5), None,
      None,
      None,
      Some(Program("p5", Some("d5"))))

    val program2 = TVContent("channel1", List("FREEVIEW", "SKY"), current.plusHours(5), current.plusHours(7), None,
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }

  object Channel2 {
    val series3 = TVContent("channel2", List("FREEVIEW", "SKY"), current.minusHours(5), current, None,
      Some(Series("serie2", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val film3 = TVContent("channel2", List("FREEVIEW", "SKY"), current, current.plusHours(3), None,
      None,
      Some(Film("film3", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val program3 = TVContent("channel2", List("FREEVIEW", "SKY"), current.plusHours(2), current.plusHours(7), None,
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }

  object Channel3 {
    val film4 = TVContent("channel3", List("FREEVIEW", "SKY"), current, current.plusHours(3), Some(8),
      None,
      Some(Film("film4", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)
  }


  object Channel4 {
    val program4 = TVContent("channel4", List("FREEVIEW", "SKY"), current.minusHours(1), current.plusHours(1), None,
      None,
      None,
      Some(Program("p6", Some("d6"))))
  }

  object Channel5 {
    val birdman = TVContent("channel5", List("WHATISNEWPROV", "SKY"), current.minusHours(3), current.plusHours(2), Some(7.8),
      None,
      Some(Film("birdman", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val boyhood = TVContent("channel5", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.4),
      None,
      Some(Film("boyhood", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val homeland = TVContent("channel5", List("WHATISNEWPROV", "SKY"), current.plusHours(3), current.plusHours(6), Some(9.4),
      Some(Series("homeland", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)
  }

  object Channel6 {
    val friends = TVContent("channel6", List("WHATISNEWPROV", "SKY"), current.minusHours(3), current.plusHours(1), None,
      Some(Series("friends", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val dexter = TVContent("channel6", List("WHATISNEWPROV", "SKY"), current.plusHours(1), current.plusHours(2), Some(8.4),
      Some(Series("dexter", Some(Episode(Some("ep1"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val world = TVContent("channel6", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.4),
      None,
      Some(Film("world", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

  }

  object Channel7 {
    val news = TVContent("channel7", List("WHATISNEWPROV", "SKY"), current.minusHours(2), current.plusHours(1), None,
      None,
      None,
      Some(Program("news", Some("d6"))))

    val weather = TVContent("channel7", List("WHATISNEWPROV", "SKY"), current.plusHours(1), current.plusHours(2), None,
      None,
      None,
      Some(Program("weather", Some("d6"))))

    val africa = TVContent("channel7", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), None,
      None,
      None,
      Some(Program("africa", Some("d6"))))
  }

  object Channel8 {
    val forrest = TVContent("channel8", List("SEARCHPROVIDER", "SKY"), current, current.plusHours(1), Some(8.4),
      None,
      Some(Film("forrest in sky", List("actor1"), List(), List(), List(), List(), None, None, None, None, None, None)),
      None)

    val sky = TVContent("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(1), current.plusHours(2), Some(8.4),
      Some(Series("sky", Some(Episode(Some("some day"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)

    val fargo = TVContent("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.5),
      Some(Series("Fargo", Some(Episode(Some("SKY in NYC"), None, None, None, None)), List("actor1"),
        List(), List(), List(), List(), None, None, None, None, None, None)),
      None,
      None)


    val europe = TVContent("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(3), current.plusHours(4), None,
      None,
      None,
      Some(Program("sky", Some("d6"))))
  }


  override def beforeAll {
    import Channel1._, Channel2._, Channel3._, Channel4._, Channel5._, Channel6._, Channel7._, Channel8._
    whenReady(tvContentRepository.removeAll()) {
      ok => println(s"Before - collection ${this.getClass.getCanonicalName} removed: $ok")
    }
    whenReady(tvContentRepository.insertBulk(
      Enumerator(series1, series2, film1, film2, program1, program2, series3, film3, program3, film4, program4,
        birdman, boyhood, homeland, friends, dexter, world, news, weather, africa, sky,
        forrest, europe, fargo))) {
      response => response mustBe 24
    }
  }

  override def afterAll {
    whenReady(tvContentRepository.removeAll()) {
      ok => println(s"After - collection ${this.getClass.getCanonicalName} removed: $ok")
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

  "findTopLeftContentByProvider" should {
    "return the 2 top TV content for provider FREEVIEW left" in {
      whenReady(tvContentRepository.findTopLeftContentByProvider(2, "FREEVIEW")) {
        _ mustBe Seq(Channel3.film4, Channel1.film1)
      }
    }
    "return the 4 top TV content for provider FREEVIEW left" in {
      whenReady(tvContentRepository.findTopLeftContentByProvider(4, "FREEVIEW")) {
        _ mustBe Seq(Channel3.film4, Channel1.film1, Channel4.program4, Channel2.film3)
      }
    }


    "return empty List of 4 top TV content for provider FREEVIEW_UKNOWN left" in {
      whenReady(tvContentRepository.findTopLeftContentByProvider(4, "FREEVIEW_UKNOWN")) {
        _ mustBe Seq()
      }
    }
  }

  "findNextProgramByProvider" should {
    "return what is next for provider WHATISNEWPROV left" in {
      whenReady(tvContentRepository.findNextProgramByProvider("WHATISNEWPROV")) {
        _ mustBe Seq(Channel6.dexter, Channel5.boyhood, Channel7.weather)
      }
    }

    "return empty List of next TV content for provider FREEVIEW_UKNOWN" in {
      whenReady(tvContentRepository.findNextProgramByProvider("FREEVIEW_UKNOWN")) {
        _ mustBe Seq()
      }
    }
  }

  "searchTitleByProvider" should {
    "return series when search by 'sky'" in {
      whenReady(tvContentRepository.searchTitleByProvider("SKY", "SEARCHPROVIDER")) {
        _ mustBe Seq(Channel8.fargo, Channel8.forrest, Channel8.sky, Channel8.europe)
      }
    }
  }
}

