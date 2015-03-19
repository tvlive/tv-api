package models

import controllers.external.TVShort
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
      val tvc = TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(1), None, None, None, Some(id))
      val tvContent = TVShort(tvc)
      tvContent.uriTVContentDetails mustBe s"http://localhost:9000/tvcontent/$idString"
    }
  }
}
