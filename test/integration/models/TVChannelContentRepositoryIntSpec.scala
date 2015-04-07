package models

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils._

class TVChannelContentRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfterAll with ScalaFutures with MongoSugar {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(3, Seconds), interval = Span(5, Millis))

  val current = new DateTime(2010, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))

  implicit val time: TimeProvider = new TimeProvider {
    override def currentDate() = current
  }

  val tvContentRepository = new TVContentRepository(this.getClass.getCanonicalName)
  Thread.sleep(3000)

  object tvguide {

    object Channel1 {
      val series1 = SeriesBuilder("channel1", List("FREEVIEW", "SKY"), current.minusHours(4), current.minusHours(2), None, "serie1", "ep1")
      val series2 = SeriesBuilder("channel1", List("FREEVIEW", "SKY"), current.minusHours(2), current, None, "serie2", "ep2")
      val film1 = FilmBuilder("channel1", List("FREEVIEW", "SKY"), current, current.plusHours(1), Some(7), "film1")
      val film2 = FilmBuilder("channel1", List("FREEVIEW", "SKY"), current.plusHours(1), current.plusHours(3), None, "film2")
      val program1 = ProgramBuilder("channel1", List("FREEVIEW", "SKY"), current.plusHours(3), current.plusHours(5), "p5")
      val program2 = ProgramBuilder("channel1", List("FREEVIEW", "SKY"), current.plusHours(5), current.plusHours(7), "p6")

      val toSeq = Seq(series1, series2, film1, film2, program1, program2)

    }

    object Channel2 {
      val series3 = SeriesBuilder("channel2", List("FREEVIEW", "SKY"), current.minusHours(5), current, None, "serie2", "ep1")
      val film3 = FilmBuilder("channel2", List("FREEVIEW", "SKY"), current, current.plusHours(3), None, "film3")
      val program3 = ProgramBuilder("channel2", List("FREEVIEW", "SKY"), current.plusHours(2), current.plusHours(7), "p6")

      val toSeq = Seq(series3, film3, program3)
    }

    object Channel3 {
      val film4 = FilmBuilder("channel3", List("FREEVIEW", "SKY"), current, current.plusHours(3), Some(8), "film4")

      val toSeq = Seq(film4)
    }

    object Channel4 {
      val program4 = ProgramBuilder("channel4", List("FREEVIEW", "SKY"), current.minusHours(1), current.plusHours(1), "p6")

      val toSeq = Seq(program4)
    }

    object Channel5 {
      val birdman = FilmBuilder("channel5", List("WHATISNEWPROV", "SKY"), current.minusHours(3), current.plusHours(2), Some(7.8), "birdman")
      val boyhood = FilmBuilder("channel5", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.4), "boyhood")
      val homeland = SeriesBuilder("channel5", List("WHATISNEWPROV", "SKY"), current.plusHours(3), current.plusHours(6), Some(9.4), "homeland", "ep1")

      val toSeq = Seq(birdman, boyhood, homeland)
    }

    object Channel6 {
      val friends = SeriesBuilder("channel6", List("WHATISNEWPROV", "SKY"), current.minusHours(3), current.plusHours(1), None, "friends", "ep1")
      val dexter = SeriesBuilder("channel6", List("WHATISNEWPROV", "SKY"), current.plusHours(1), current.plusHours(2), Some(8.4), "dexter", "ep1")
      val world = FilmBuilder("channel6", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.4), "world")

      val toSeq = Seq(friends, dexter, world)
    }

    object Channel7 {
      val news = ProgramBuilder("channel7", List("WHATISNEWPROV", "SKY"), current.minusHours(2), current.plusHours(1), "news")
      val weather = ProgramBuilder("channel7", List("WHATISNEWPROV", "SKY"), current.plusHours(1), current.plusHours(2), "weather")
      val africa = ProgramBuilder("channel7", List("WHATISNEWPROV", "SKY"), current.plusHours(2), current.plusHours(3), "africa")

      val toSeq = Seq(news, weather, africa)
    }

    object Channel8 {
      val forrest = FilmBuilder("channel8", List("SEARCHPROVIDER", "SKY"), current, current.plusHours(1), Some(8.4), "forrest in sky")
      val sky = SeriesBuilder("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(1), current.plusHours(2), Some(8.4), "sky", "some day")
      val fargo = SeriesBuilder("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(2), current.plusHours(3), Some(8.5), "Fargo", "SKY in NYC")
      val europe = ProgramBuilder("channel8", List("SEARCHPROVIDER", "SKY"), current.plusHours(3), current.plusHours(4), "sky")

      val toSeq = Seq(forrest, sky, fargo, europe)
    }

    val toSeq = Channel1.toSeq ++ Channel2.toSeq ++ Channel3.toSeq ++ Channel4.toSeq ++ Channel5.toSeq ++
      Channel6.toSeq ++ Channel7.toSeq ++ Channel8.toSeq


  }

  override def beforeAll {
    whenReady(tvContentRepository.removeAll()) {
      ok => println(s"Before - collection ${this.getClass.getCanonicalName} removed: $ok")
    }
    whenReady(tvContentRepository.insertBulk(Enumerator(tvguide.toSeq: _*))) {
      response => response mustBe tvguide.toSeq.length
    }
  }

  override def afterAll {
    whenReady(tvContentRepository.drop) {
      ok => println(s"After - collection ${this.getClass.getCanonicalName} dropped: $ok")
    }
  }


  "findDayContentByChannel" should {
    "return all the TV content for channel1 available today" in {
      whenReady(tvContentRepository.findDayContentByChannel("channel1")) {
        import tvguide.Channel1._
        _ mustBe Seq(series1, series2, film1, film2, program1, program2)
      }
    }
  }

  "findCurrentContentByChannel" should {
    "return the TV content for channel1 available now" in {
      whenReady(tvContentRepository.findCurrentContentByChannel("channel1")) {
        import tvguide.Channel1._
        _ mustBe Some(film1)
      }
    }
  }

  "findLeftContentByChannel" should {
    "return the TV content for channel 1 available from now until the end of the day" in {
      whenReady(tvContentRepository.findLeftContentByChannel("channel1")) {
        import tvguide.Channel1._
        _ mustBe Seq(film1, film2, program1, program2)
      }
    }
  }

  "findContentByID" should {
    "return some TV Content for a particular ID" in {
      import tvguide.Channel1._
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
      import tvguide._
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("series", "FREEVIEW")) {
        _ mustBe Seq(Channel2.series3, Channel1.series1, Channel1.series2)
      }
    }

    "return all the TV content for a type film and provider FREEVIEW" in {
      import tvguide._
      whenReady(tvContentRepository.findDayContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(Channel1.film1, Channel2.film3, Channel3.film4, Channel1.film2)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW" in {
      import tvguide._
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
      import tvguide._
      whenReady(tvContentRepository.findCurrentContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(Channel3.film4, Channel1.film1, Channel2.film3)
      }
    }

    "return all the TV content for a type program and provider FREEVIEW available now" in {
      import tvguide._
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
      import tvguide._
      whenReady(tvContentRepository.findLeftContentByTypeAndProvider("film", "FREEVIEW")) {
        _ mustBe Seq(Channel1.film1, Channel2.film3, Channel3.film4, Channel1.film2)
      }
    }

    "return all the TV content left for a type program and provider FREEVIEW" in {
      import tvguide._
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
      import tvguide._
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
      import tvguide._
      whenReady(tvContentRepository.findTopLeftContentByProvider(2, "FREEVIEW")) {
        _ mustBe Seq(Channel3.film4, Channel1.film1)
      }
    }
    "return the 4 top TV content for provider FREEVIEW left" in {
      import tvguide._
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
      import tvguide._
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
    "return when search by 'sky'" in {
      import tvguide._
      whenReady(tvContentRepository.searchBy("SEARCHPROVIDER", Some("SKY"), None, None)) {
        _ mustBe Seq(Channel8.fargo, Channel8.forrest, Channel8.sky, Channel8.europe)
      }
    }

    "return series when search by 'sky'" in {
      import tvguide._
      whenReady(tvContentRepository.searchBy("SEARCHPROVIDER", Some("SKY"), Some("series"), None)) {
        _ mustBe Seq(Channel8.fargo, Channel8.sky)
      }
    }

    "return program when search by 'sky'" in {
      import tvguide._
      whenReady(tvContentRepository.searchBy("SEARCHPROVIDER", Some("SKY"), Some("program"), None)) {
        _ mustBe Seq(Channel8.europe)
      }
    }

    "return film when search by 'sky'" in {
      import tvguide._
      whenReady(tvContentRepository.searchBy("SEARCHPROVIDER", Some("SKY"), Some("film"), None)) {
        _ mustBe Seq(Channel8.forrest)
      }
    }

    "return content with rating when search by 'sky'" in {
      import tvguide._
      whenReady(tvContentRepository.searchBy("SEARCHPROVIDER", Some("SKY"), None, Some(8.4))) {
        _ mustBe Seq(Channel8.forrest, Channel8.sky)
      }
    }
  }
}

