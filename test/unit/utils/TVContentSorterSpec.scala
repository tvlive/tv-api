package utils

import models.TVContent
import org.joda.time.{DateTimeZone, DateTime}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec

class TVContentSorterSpec extends PlaySpec with MustMatchers with TVContentSorter {
  val current = new DateTime(2010, 10, 10, 10, 0, 0, DateTimeZone.forID("UTC"))
  "sortedBy" should {

    "return true when the first has rating and second has no rating" in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
      end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = None, None, None, None, None)

      sortedBy(f,s) mustBe true
    }

    "return true when the first has rating higher than second " in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = Some(7.4), None, None, None, None)

      sortedBy(f,s) mustBe true
    }

    "return true when the first has same rating as second but starts earlier" in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(2),
        end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      sortedBy(f,s) mustBe true
    }

    "return true when the first has same rating as second, the first starts same as the second but the channel name of the first is less or eq than the second" in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = Some(7.5), None, None, None, None)

      sortedBy(f,s) mustBe true
    }

  "return true when the first and second have no ratings but the first starts earlier than the second" in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = None, None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(2),
        end = current.plusHours(3), rating = None, None, None, None, None)

      sortedBy(f,s) mustBe true
    }

    "return true when the first and second have no ratings, the first starts same as the second but the channel name of the first is less or eq than the second" in {
      val f = TVContent(channel = "channel1", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = None, None, None, None, None)

      val s = TVContent(channel = "channel2", provider = List("provider1"), start = current.plusHours(1),
        end = current.plusHours(2), rating = None, None, None, None, None)

      sortedBy(f,s) mustBe true
    }
  }
}
