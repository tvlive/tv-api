package integration.models

import models.{MongoSugar, TVChannelProviderRepository, TVChannelProvider}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Span, Seconds}
import org.scalatest.{BeforeAndAfterAll, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID

class TVChannelProviderRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfterAll with ScalaFutures with MongoSugar {

  val tvChannelProvider1 = TVChannelProvider("FREEVIEW", Some(BSONObjectID.generate))
  val tvChannelProvider2 = TVChannelProvider("SKY", Some(BSONObjectID.generate))
  val tvChannelProvider3 = TVChannelProvider("TERRESTRIAL", Some(BSONObjectID.generate))
  val tvChannelProvider4 = TVChannelProvider("PROVIDER", Some(BSONObjectID.generate))


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(3, Seconds), interval = Span(5, Millis))

  val tvChannelProviderRepository: TVChannelProviderRepository = new TVChannelProviderRepository(this.getClass.getCanonicalName)

  override def beforeAll {
    whenReady(tvChannelProviderRepository.removeAll()){
      ok => println(s"Before - collection ${this.getClass.getCanonicalName} removed: $ok")
    }

    whenReady(tvChannelProviderRepository.insertBulk(
      Enumerator(
        tvChannelProvider1,
        tvChannelProvider2,
        tvChannelProvider3,
        tvChannelProvider4))) {
      response => response mustBe 4
    }
  }

  override def afterAll {
    whenReady(tvChannelProviderRepository.drop){
      ok => println(s"After - collection ${this.getClass.getCanonicalName} dropped: $ok")
    }
  }

  "findAll" should {
    "return all the providers for the tv channels order alphabetically" in {

      whenReady(tvChannelProviderRepository.findAll()) {
        _ mustBe Seq(tvChannelProvider1, tvChannelProvider4, tvChannelProvider2, tvChannelProvider3)
      }
    }
  }

}
