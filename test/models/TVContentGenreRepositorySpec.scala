package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import utils.MongoSugar

class TVContentGenreRepositorySpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures with MongoSugar {

  val tvContentGenre1 = TVContentGenre("SPORTS", Some(BSONObjectID.generate))
  val tvContentGenre2 = TVContentGenre("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvContentGenre3 = TVContentGenre("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvContentGenre4 = TVContentGenre("NEWS", Some(BSONObjectID.generate))


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvContentGenreRepository: TVContentGenreRepository = new TVContentGenreRepository(this.getClass.getCanonicalName)
  tvContentGenreRepository.drop()
  Thread.sleep(5000)

  before {
    whenReady(tvContentGenreRepository.insertBulk(
      Enumerator(
        tvContentGenre1,
        tvContentGenre2,
        tvContentGenre3,
        tvContentGenre4))) {
      response => response mustBe 4
    }
  }

  after {
    whenReady(tvContentGenreRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
    }
  }

  "findAll" should {
    "return all the genres for the tv contents order alphabetically" in {

      whenReady(tvContentGenreRepository.findAll()) {
        _ mustBe Seq(tvContentGenre3, tvContentGenre2, tvContentGenre4, tvContentGenre1)
      }
    }
  }

}
