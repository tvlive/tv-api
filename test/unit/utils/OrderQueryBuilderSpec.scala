package utils

import org.joda.time.DateTime
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import reactivemongo.bson._

class OrderQueryBuilderSpec extends PlaySpec with MustMatchers {

  import _root_.utils.mongoBuilder._
  "ratingDesc" should {
    "add order condition: rating descendent" in {
      val query = BSONDocument().ratingDesc()
      query.get("rating") mustBe Some(BSONInteger(-1))
    }
  }

  "startAsc" should {
    "add order condition: start time ascendent" in {
      val query = BSONDocument().startAsc()
      query.get("start") mustBe Some(BSONInteger(1))
    }
  }

  "channelAsc" should {
    "add order condition: channel name ascendent" in {
      val query = BSONDocument().channelAsc()
      query.get("channel") mustBe Some(BSONInteger(1))
    }
  }

  "providerAsc" should {
    "add order condition: tv provider ascendent" in {
      val query = BSONDocument().providerAsc()
      query.get("provider") mustBe Some(BSONInteger(1))
    }
  }

  "categoryAsc" should {
    "add order condition: tv category ascendent" in {
      val query = BSONDocument().categoryAsc()
      query.get("category") mustBe Some(BSONInteger(1))
    }
  }

  "nameAsc" should {
    "add order condition: channel name ascendent" in {
      val query = BSONDocument().nameAsc()
      query.get("name") mustBe Some(BSONInteger(1))
    }
  }
}
