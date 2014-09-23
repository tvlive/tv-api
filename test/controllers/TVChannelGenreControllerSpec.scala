package controllers

import models.{TVChannelGenreRepository, TVChannelGenre}
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span, Millis}
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.iteratee.Enumerator
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID
import utils.MongoSugar

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class TVChannelGenreControllerSpec extends Specification with TVChannelGenreSetUpTest {


  "TVChannelGenreController" should {

    "provide the all the list of genres availables for tv channels order alphabetically" in {
      val genresResult: Future[SimpleResult] = controller.genres().apply(FakeRequest())
      status(genresResult) must equalTo(OK)
      contentType(genresResult) must beSome.which(_ == "application/json")
      val genresInResponse = contentAsJson(genresResult).as[Seq[TVChannelGenre]]
      genresInResponse mustEqual Seq(tvChannelGenre3, tvChannelGenre2, tvChannelGenre4, tvChannelGenre1)

    }
  }
}


trait TVChannelGenreSetUpTest extends ScalaFutures with MongoSugar {

  self: Specification =>

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val tvChannelGenreRepository = new TVChannelGenreRepository(this.getClass.getCanonicalName)
  tvChannelGenreRepository.drop()
  Thread.sleep(5000)
//
  val tvChannelGenre1 = TVChannelGenre("SPORTS", Some(BSONObjectID.generate))
  val tvChannelGenre2 = TVChannelGenre("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvChannelGenre3 = TVChannelGenre("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvChannelGenre4 = TVChannelGenre("NEWS", Some(BSONObjectID.generate))

  whenReady(tvChannelGenreRepository.insertBulk(
    Enumerator(
      tvChannelGenre1,
      tvChannelGenre2,
      tvChannelGenre3,
      tvChannelGenre4))) {
    response => response must_== 4
  }


  class App extends controllers.TVChannelGenreController {
    override val channelGenreReporitory: TVChannelGenreRepository = tvChannelGenreRepository
  }

  val controller = new App

}
