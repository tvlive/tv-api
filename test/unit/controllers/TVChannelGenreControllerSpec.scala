package controllers

import models.{ChannelGenreRepository, TVChannelCategory}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

class TVChannelGenreControllerSpec extends PlaySpec with MustMatchers with TVChannelGenreSetUpTest {


  "TVChannelGenreController" should {

    "provide the all the list of genres availables for tv channels order alphabetically" in {
      val genresResult: Future[SimpleResult] = controller.genres().apply(FakeRequest())
      status(genresResult) mustBe(OK)
      contentType(genresResult) mustBe(Some("application/json"))
      val genresInResponse = contentAsJson(genresResult).as[Seq[TVChannelCategory]]
      genresInResponse mustEqual Seq(tvChannelGenre3, tvChannelGenre2, tvChannelGenre4, tvChannelGenre1)

    }
  }
}


trait TVChannelGenreSetUpTest {

  val tvChannelGenre1 = TVChannelCategory("SPORTS", Some(BSONObjectID.generate))
  val tvChannelGenre2 = TVChannelCategory("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvChannelGenre3 = TVChannelCategory("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvChannelGenre4 = TVChannelCategory("NEWS", Some(BSONObjectID.generate))

  val tvChannelGenreRepository = new ChannelGenreRepository() {
    override def findAll(): Future[Seq[TVChannelCategory]] = {
      Future.successful(Seq(tvChannelGenre3, tvChannelGenre2, tvChannelGenre4, tvChannelGenre1))
    }
  }

  class App extends controllers.TVChannelCategoryController {
    override val channelGenreReporitory = tvChannelGenreRepository
  }

  val controller = new App

}
