package utils

import org.joda.time.DateTime
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import reactivemongo.bson._

class SearchQueryBuilderSpec extends PlaySpec with MustMatchers {

  import _root_.utils.mongoBuilder._

  "rating" should {
    "add search condition: rating '7.8'" in {
      val query = BSONDocument().rating(7.8)
      query.get("rating") mustBe Some(BSONDouble(7.8))
    }
  }

  "id" should {
    "add search condition: id '55133bc701000001006ab668'" in {
      val query = BSONDocument().id("55133bc701000001006ab668")
      query.get("_id") mustBe Some(BSONObjectID("55133bc701000001006ab668"))
    }
  }

  "provider" should {
    "add search condition: provider 'freeview'" in {
      val query = BSONDocument().provider("freeview")
      query.get("provider") mustBe Some(BSONString("freeview"))
    }
  }

  "channel" should {
    "add search condition: channel 'BBC ONE'" in {
      val query = BSONDocument().channel("BBC ONE")
      query.get("channel") mustBe Some(BSONString("BBC ONE"))
    }
  }

  "category" should {
    "add search condition: category 'SPORTS'" in {
      val query = BSONDocument().category("SPORTS")
      query.get("category") mustBe Some(BSONString("SPORTS"))
    }
  }

  "title" should {
    "add search condition: title 'sky'" in {
      val query = BSONDocument().title("sky")
      query.get("$text") mustBe Some(BSONDocument("$search" -> "sky"))
    }
  }

  "startLte" should {
    "add search condition: start time to be lte the time passed as a parameter" in {
      val now = new DateTime(2015, 11, 4, 0, 0)
      val query = BSONDocument().startLte(now)
      query.get("start") mustBe Some(BSONDocument("$lte" -> BSONDateTime(now.getMillis)))
    }
  }

  "startGt" should {
    "add search condition: start time to be gt the time passed as a parameter" in {
      val now = new DateTime(2015, 11, 4, 0, 0)
      val query = BSONDocument().startGt(now)
      query.get("start") mustBe Some(BSONDocument("$gt" -> BSONDateTime(now.getMillis)))
    }
  }

  "endGt" should {
    "add search condition: end time to be gt the time passed as a parameter" in {
      val now = new DateTime(2015, 11, 4, 0, 0)
      val query = BSONDocument().endGt(now)
      query.get("end") mustBe Some(BSONDocument("$gt" -> BSONDateTime(now.getMillis)))
    }
  }

  "exist" should {
    "add search condition: the exist key " in {
      val query = BSONDocument().exist("series")
      query.get("series") mustBe Some(BSONDocument("$exists" -> true))
    }
  }
}
