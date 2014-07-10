package models

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class TVChannelRepositorySpec extends PlaySpec with MustMatchers{

  val tvChannelRepository = FakeTVChannelRepositoy

  "listOfTVChannels" should {
    "return the lis tof TV Channels available today" in {
      val allTVChannels = tvChannelRepository.listOfTVChannels()
      allTVChannels mustBe  Seq(
        TVChannel("Channel1", "EN"),
        TVChannel("Channel2", "EN"),
        TVChannel("Channel3", "EN"),
        TVChannel("Channel4", "EN")
      )
    }
  }

}
