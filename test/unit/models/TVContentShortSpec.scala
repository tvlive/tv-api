package models

import controllers.external.{TVLong, TVShort}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import reactivemongo.bson.BSONObjectID
import utils.TimeProvider

class TVContentShortSpec extends PlaySpec with MustMatchers {
  val now = new DateTime(DateTimeZone.forID("UTC"))
  val id = BSONObjectID.generate
  val idString = id.stringify
  implicit val host: String = "http://localhost:9000"
  implicit val time: TimeProvider = new TimeProvider {
    override def currentDate() = now
  }

  "uriTVContentDetails" should {
    "be true when tvContent start time is before now and after time is after now" in {
      val tvc = TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(1), None, None, None, None, Some(id))
      val tvContent = TVShort(tvc)
      tvContent.uriTVContentDetails mustBe s"http://localhost:9000/tvcontent/$idString"
    }
  }

  "poster in a film" should {
    "be http://localhost:9000/images/123456789" in {
      val content = TVShort(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None, None,
          Some(Film(title = "some title", List(), List(), List(), List(), List(), None, None, None, None, None, imdbId = Some("123456789"))),
          None))
      content.film.get.poster mustBe Some("http://localhost:9000/images/123456789")
    }
  }

  "poster in a series" should {
    "be http://localhost:9000/images/987654321" in {
      val content = TVShort(
        TVContent("channel1", List("provider1"), now.plusHours(2), now.plusHours(3), None,
          Some(Series(serieTitle = "some title", None, List(), List(), List(), List(), List(), None, None, None, None, None, imdbId = Some("987654321"))),
          None, None))
      content.series.get.poster mustBe Some("http://localhost:9000/images/987654321")
    }
  }
}
