package models

import controllers.external.ChannelLong
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class TVChannelSpec extends PlaySpec with MustMatchers {
  val channel = ChannelLong(TVChannel("CHANNEL 1 PLUS", List(), List(), None))("http://localhost:9000")

  "image" should {
    "be http://localhost:9000/CHANNEL_1_PLUS.png when tvChannel is 'CHANNEL 1 PLUS'" in {
      channel.image mustBe "http://localhost:9000/CHANNEL_1_PLUS.png"
    }
  }

  "uriToday" should {
    "be http://localhost:9000/channels/CHANNEL_1_PLUS/today when tvChannel 'CHANNEL 1 PLUS'" in {
      channel.uriToday mustBe "http://localhost:9000/tvcontent/channel/CHANNEL+1+PLUS/today"
    }
  }

  "uriCurrent" should {
    "be http://localhost:9000/channels/CHANNEL_1_PLUS/current when tvChannel 'CHANNEL 1 PLUS'" in {
      channel.uriCurrent mustBe "http://localhost:9000/tvcontent/channel/CHANNEL+1+PLUS/current"
    }
  }

  "uriLeft" should {
    "be http://localhost:9000/channels/CHANNEL_1_PLUS/left when tvChannel 'CHANNEL 1 PLUS'" in {
      channel.uriLeft mustBe "http://localhost:9000/tvcontent/channel/CHANNEL+1+PLUS/left"
    }
  }
}
