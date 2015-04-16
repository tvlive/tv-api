package models

import controllers.external.TVLong
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import utils.TimeProvider

class TVContentLongSpec extends PlaySpec with MustMatchers {
  val now = new DateTime(DateTimeZone.forID("UTC"))
  implicit val host: String = "http://localhost:9000"
  implicit val time: TimeProvider = new TimeProvider {
    override def currentDate() = now
  }
  "onTimeNow" should {
    "be true when tvContent start time is before now and after time is after now" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(1), None, None, None, None))
      content.onTimeNow mustBe true
    }

    "be false when tvContent start time and after time before now" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.minusHours(2), now.minusHours(1), None, None, None, None))
      content.onTimeNow mustBe false
    }

    "be false when tvContent start time and after time after now" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.plusHours(1), now.plusHours(2), None, None, None, None))
      content.onTimeNow mustBe false
    }
  }

  "perCentTimeElapsed" should {
    "be 50% when tvContent started 1 hour ago and finishes in 1 hour" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(1), None, None, None, None))
      content.perCentTimeElapsed mustBe Some(50)
    }

    "be 20% when tvContent started 1 hour ago and finishes in 4 hour" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(4), None, None, None, None))
      content.perCentTimeElapsed mustBe Some(20)
    }

    "be None when tvContent started 2 hour ago and finishes in 1 hour" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.minusHours(2), now.minusHours(1), None, None, None, None))
      content.perCentTimeElapsed mustBe None
    }

    "be None when tvContent starts in 2 hours and finishes in 3 hour" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None, None, None, None))
      content.perCentTimeElapsed mustBe None
    }
  }

  "channelImageURL" should {
    "be channel1.png when the channel is channel1" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None, None, None, None))
      content.channelImageURL mustBe "http://localhost:9000/channel1.png"
    }

    "be channel_exampe.png when the channel is channel example" in {
      val content = TVLong(
        TVContent("channel example", List("provider1"), now.plusHours(2), now.plusHours(3), None, None, None, None))
      content.channelImageURL mustBe "http://localhost:9000/channel_example.png"
    }
  }

  "poster in a film" should {
    "be http://localhost:9000/images/123456789" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None, None,
          Some(Film(title = "some title", List(), List(), List(), List(), List(), None, None, None, None, None, imdbId = Some("123456789"))),
          None))
      content.film.get.poster mustBe Some("http://localhost:9000/images/123456789")
    }
  }

  "poster in a series" should {
    "be http://localhost:9000/images/987654321" in {
      val content = TVLong(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None,
          Some(Series(serieTitle = "some title", None, List(), List(), List(), List(), List(), None, None, None, None, None, imdbId = Some("987654321"))),
          None, None))
      content.series.get.poster mustBe Some("http://localhost:9000/images/987654321")
    }
  }

}
