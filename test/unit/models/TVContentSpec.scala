package models

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class TVContentSpec extends PlaySpec with MustMatchers {
  val now = new DateTime(DateTimeZone.forID("UTC"))

  "onTimeNow" should {
    "be true when tvContent start time is before now and after time is after now" in {
      val content = TVContent("channel1", List("provider1"), now.minusHours(1), now.plusHours(1), None, None, None, None, None)
      content.onTimeNow mustBe true
    }

    "be false when tvContent start time and after time before now" in {
      val content = TVContent("channel1", List("provider1"), now.minusHours(2), now.minusHours(1), None, None, None, None, None)
      content.onTimeNow mustBe false
    }

    "be false when tvContent start time and after time after now" in {
      val content = TVContent("channel1", List("provider1"), now.plusHours(1), now.plusHours(2), None, None, None, None, None)
      content.onTimeNow mustBe false
    }
  }

}
