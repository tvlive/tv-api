package controllers

import models.{ContentGenreReporitory, TVContentGenre, TVChannelGenre}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

class TVContentGenreControllerSpec extends PlaySpec with MustMatchers with TVContentGenreSetUpTest {


  "TVContentGenreController" should {

    "provide the all the list of genres availables for tv content ordered alphabetically" in {
      val genresResult: Future[SimpleResult] = controller.genres().apply(FakeRequest())
      status(genresResult) mustBe(OK)
      contentType(genresResult) mustBe(Some("application/json"))
      val genresInResponse = contentAsJson(genresResult).as[Seq[TVContentGenre]]
      genresInResponse mustEqual Seq(tvContentGenre3, tvContentGenre2, tvContentGenre4, tvContentGenre1)

    }
  }
}


trait TVContentGenreSetUpTest {

  val tvContentGenre1 = TVContentGenre("SPORTS", Some(BSONObjectID.generate))
  val tvContentGenre2 = TVContentGenre("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvContentGenre3 = TVContentGenre("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvContentGenre4 = TVContentGenre("NEWS", Some(BSONObjectID.generate))

  val tvContentGenreRepository = new ContentGenreReporitory() {
    override def findAll(): Future[Seq[TVContentGenre]] = {
      Future.successful(Seq(tvContentGenre3, tvContentGenre2, tvContentGenre4, tvContentGenre1))
    }
  }

  class App extends controllers.TVContentGenreController {
    override val contentGenreReporitory = tvContentGenreRepository
  }

  val controller = new App

}
