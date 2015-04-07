package models

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID

class TVChannelCategoryRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfterAll with ScalaFutures with MongoSugar {

  val tvChannelCategory1 = TVChannelCategory("SPORTS", Some(BSONObjectID.generate))
  val tvChannelCategory2 = TVChannelCategory("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvChannelCategory3 = TVChannelCategory("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvChannelCategory4 = TVChannelCategory("NEWS", Some(BSONObjectID.generate))


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(3, Seconds), interval = Span(5, Millis))

  val tvChannelCategoryRepository: TVChannelCategoryRepository = new TVChannelCategoryRepository(this.getClass.getCanonicalName)

  override def beforeAll {
    whenReady(tvChannelCategoryRepository.removeAll()){
      ok => println(s"Before - collection ${this.getClass.getCanonicalName} removed: $ok")
    }
    whenReady(tvChannelCategoryRepository.insertBulk(
      Enumerator(
        tvChannelCategory1,
        tvChannelCategory2,
        tvChannelCategory3,
        tvChannelCategory4))) {
      response => response mustBe 4
    }
  }

  override def afterAll {
    whenReady(tvChannelCategoryRepository.drop){
      ok => println(s"After - collection ${this.getClass.getCanonicalName} dropped: $ok")
    }
  }

  "findAll" should {
    "return all the categories for the tv channels order alphabetically" in {

      whenReady(tvChannelCategoryRepository.findAll()) {
        _ mustBe Seq(tvChannelCategory3, tvChannelCategory2, tvChannelCategory4, tvChannelCategory1)
      }
    }
  }

}
