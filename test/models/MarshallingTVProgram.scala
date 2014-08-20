package models

import org.joda.time.DateTime
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVProgram extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify
  val now = new DateTime(2014,10,10,10,0,0)

  "Write and reads" should {
    "transform TVProgram object to json" in {

      Json.toJson(TVProgram("bbc1", "bbc news", now, now.plusHours(2), Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Some(Program("program1", None)), Some(id))).toString() mustBe
        s"""{"channelName":"bbc1","programName":"bbc news","start":1412931600000,"end":1412938800000,"category":"documentary","flags":"flags1","serie":{"serieTitle":"serie1","description":null,"seasonNumber":null,"episodeNumber":null,"totalNumber":null},"program":{"title":"program1","description":null},"id":\"$idString\"}"""
    }

    "transform json to TVProgram object" in {
      Json.parse(s"""{"channelName":"bbc1","programName":"bbc news","start":1412931600000,"end":1412938800000,"category":"documentary","flags":"flags1","serie":{"serieTitle":"serie1","description":null,"seasonNumber":null,"episodeNumber":null,"totalNumber":null},"program":{"title":"program1","description":null},"id":\"$idString\"}""")
        .as[TVProgram] mustBe TVProgram("bbc1", "bbc news", now, now.plusHours(2), Some("documentary"), Some("flags1"), Some(Serie("serie1", None, None, None, None)), Some(Program("program1", None)), Some(id))
    }
  }
}
