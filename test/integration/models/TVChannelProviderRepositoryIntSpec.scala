package integration.models

import models.{MongoSugar, TVChannelProviderRepository, TVChannelProvider}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Span, Seconds}
import org.scalatest.{BeforeAndAfter, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID

class TVChannelProviderRepositoryIntSpec extends PlaySpec with MustMatchers with BeforeAndAfter with ScalaFutures with MongoSugar {

  val tvChannelProvider1 = TVChannelProvider("FREEVIEW", Some(BSONObjectID.generate))
  val tvChannelProvider2 = TVChannelProvider("SKY", Some(BSONObjectID.generate))
  val tvChannelProvider3 = TVChannelProvider("TERRESTRIAL", Some(BSONObjectID.generate))
  val tvChannelProvider4 = TVChannelProvider("PROVIDER", Some(BSONObjectID.generate))


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelProviderRepository: TVChannelProviderRepository = new TVChannelProviderRepository(this.getClass.getCanonicalName)
  tvChannelProviderRepository.drop()
  Thread.sleep(5000)

  before {
    whenReady(tvChannelProviderRepository.insertBulk(
      Enumerator(
        tvChannelProvider1,
        tvChannelProvider2,
        tvChannelProvider3,
        tvChannelProvider4))) {
      response => response mustBe 4
    }
  }

  after {
    whenReady(tvChannelProviderRepository.drop()) {
      response => println(s"Collection ${this.getClass.getCanonicalName} has been drop: $response")
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
