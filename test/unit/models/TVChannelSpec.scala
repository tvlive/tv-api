package models

import controllers.external.ChannelLong
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class TVChannelSpec extends PlaySpec with MustMatchers {
  val now = new DateTime(DateTimeZone.forID("UTC"))

  "image" should {
    "be /CHANNEL_1_PLUS.png when tvChannel CHANNEL 1 PLUS" in {
      val channel = ChannelLong(TVChannel("CHANNEL 1 PLUS", List(), List(), None))
      channel.image mustBe "http://localhost:9000/CHANNEL_1_PLUS.png"
    }
  }

  //TODO add tests for uriCurrent, uriLeft, uriToday

}
