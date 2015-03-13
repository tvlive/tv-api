package controllers

import controllers.external.{ChannelCategoryExternal, TVChannelCategoryExternal}
import models.{ChannelCategoryRepository, TVChannelCategory}
import org.mockito.Mockito._
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TVChannelCategoryControllerSpec extends PlaySpec with MustMatchers {

  "TVChannelGenreController" should {

    "provide the all the list of cateogry availables for tv channels order alphabetically" in new TVChannelCategorySetUpTest() {
      //GIVEN
      when(tvChannelGenreRepository.findAll()).thenReturn(Future(Seq(tvChannelGenre3, tvChannelGenre2, tvChannelGenre4, tvChannelGenre1)))

      //WHEN
      val genresResult: Future[SimpleResult] = controller.categories().apply(FakeRequest())

      //THEN
      status(genresResult) mustBe(OK)
      contentType(genresResult) mustBe(Some("application/json"))
      val genresInResponse = contentAsJson(genresResult).as[Seq[TVChannelCategoryExternal]]
      genresInResponse mustEqual Seq(tvChannelExtGenre3, tvChannelExtGenre2, tvChannelExtGenre4, tvChannelExtGenre1)

      verify(tvChannelGenreRepository).findAll()
    }
  }
}


trait TVChannelCategorySetUpTest extends MockitoSugar {

  val tvChannelGenre1 = TVChannelCategory("SPORTS", Some(BSONObjectID.generate))
  val tvChannelGenre2 = TVChannelCategory("ENTERTAINMENT", Some(BSONObjectID.generate))
  val tvChannelGenre3 = TVChannelCategory("DOCUMENTARY", Some(BSONObjectID.generate))
  val tvChannelGenre4 = TVChannelCategory("NEWS", Some(BSONObjectID.generate))

  val tvChannelExtGenre1 = ChannelCategoryExternal(tvChannelGenre1)
  val tvChannelExtGenre2 = ChannelCategoryExternal(tvChannelGenre2)
  val tvChannelExtGenre3 = ChannelCategoryExternal(tvChannelGenre3)
  val tvChannelExtGenre4 = ChannelCategoryExternal(tvChannelGenre4)

  val tvChannelGenreRepository = mock[ChannelCategoryRepository]

  class App extends controllers.TVChannelCategoryController {
    override val channelCategoryReporitory = tvChannelGenreRepository
  }

  val controller = new App

}
