package models

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class MarshallingTVProgram extends PlaySpec with MustMatchers {
  val id = BSONObjectID.generate
  val idString = id.stringify

  "Write and reads" should {
    "transform TVProgram object to json" in {
      Json.toJson(TVProgram("bbc1", "bbc news", 1, 2, "documentary", Some(id))).toString() mustBe
        s"""{"channelName":"bbc1","programName":"bbc news","start":1,"end":2,"typeProgram":"documentary","id":\"$idString\"}"""
    }

    "transform json to TVProgram object" in {
      Json.parse(s"""{"channelName":"bbc1","programName":"bbc news","start":1,"end":2,"typeProgram":"documentary","id":\"$idString\"}""")
        .as[TVProgram] mustBe TVProgram("bbc1", "bbc news", 1, 2, "documentary", Some(id))
    }
  }
}
