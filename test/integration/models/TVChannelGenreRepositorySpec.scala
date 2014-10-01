package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.MongoSugar

class TVChannelGenreRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures with MongoSugar {

  val tvChannelGenre1 = TVChannelGenre("SPORTS", Some(BSONObjectID.generate))
  val tvChannelGenre2 = TVChannelGenre("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvChannelGenre3 = TVChannelGenre("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvChannelGenre4 = TVChannelGenre("NEWS", Some(BSONObjectID.generate))


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelGenreRepository: TVChannelGenreRepository = new TVChannelGenreRepository(this.getClass.getCanonicalName)
  tvChannelGenreRepository.drop()
  Thread.sleep(5000)

  before {
    whenReady(tvChannelGenreRepository.insertBulk(
      Enumerator(
        tvChannelGenre1,
        tvChannelGenre2,
        tvChannelGenre3,
        tvChannelGenre4))) {
      response => response mustBe 4
    }
  }

  after {
    whenReady(tvChannelGenreRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
    }
  }

  "findAll" should {
    "return all the genres for the tv channels order alphabetically" in {

      whenReady(tvChannelGenreRepository.findAll()) {
        _ mustBe Seq(tvChannelGenre3, tvChannelGenre2, tvChannelGenre4, tvChannelGenre1)
      }
    }
  }

}
