package controllers

import models._
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID
import utils.DomainBuilder._

import scala.concurrent.Future

class TVContentControllerSpec extends PlaySpec with MustMatchers with TVContentSetUpTest {


  "TVContentController" should {

    "return NOT_FOUND if there is no TV content for CHANNEL2 available today" in {

      val contentsResult: Future[SimpleResult] = controller.allContent("CHANNEL2").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content for the channel: CHANNEL2"))
    }

    "return all the TV content for a channel1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("channel1").apply(FakeRequest())
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram1))
      tvprograms must contain(TVShortWithTimeZone(tvProgram2))
      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))

    }

    "return all the TV content for a CHANNEL 3 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL+3").apply(FakeRequest())
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram6))
    }


    "return all the TV content for a CHANNEL1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL1").apply(FakeRequest())
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram1))
      tvprograms must contain(TVShortWithTimeZone(tvProgram2))
      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))
    }
    //
    "return NOT_FOUND if there is no TV content for CHANNEL2 available now" in {

      val contentsResult: Future[SimpleResult] = controller.currentContent("CHANNEL2").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content at this moment for the channel: CHANNEL2"))
    }

    "return the TV content for CHANNEL1 available now" in {

      val programsResult: Future[SimpleResult] = controller.currentContent("CHANNEL1").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprogram = Json.parse(programInResponse).as[TVContent]
      tvprogram mustBe (TVProgramWithTimeZone(tvProgram3))
    }

    "return the TV content for CHANNEL1 available from now until the end of the day" in {

      val programsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL1").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]

      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))

    }

    "return NOT_FOUND if there is no TV content for CHANNEL2 available from now until the end of the day" in {

      val contentsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL2").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content left for the channel: CHANNEL2"))
    }

    "return TV content details for a specific TV Content ID" in {

      val programResult: Future[SimpleResult] = controller.tvContentDetails(tvProgram1.id.get.stringify).apply(FakeRequest())
      status(programResult) mustBe (OK)
      val programInResponse = contentAsString(programResult)
      val tvprogram = Json.parse(programInResponse).as[TVContent]
      tvprogram mustBe (TVProgramWithTimeZone(tvProgram1))
    }

    "return NOT_FOUND if there is no TV content details for a specific TV Content ID" in {

      val contentsResult: Future[SimpleResult] = controller.tvContentDetails("noExistID").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content details with id: noExistID"))
    }

    "return all the TV content available today by type SERIES with upper case" in {
      val programsResult: Future[SimpleResult] = controller.contentByType("SERIES").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram1), TVShortWithTimeZone(tvProgram2), TVShortWithTimeZone(tvProgram7))
    }

    "return all the TV content available today by type film" in {
      val programsResult: Future[SimpleResult] = controller.contentByType("film").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram3), TVShortWithTimeZone(tvProgram4), TVShortWithTimeZone(tvProgram8))
    }

    "return all the TV content available today by type program" in {
      val programsResult: Future[SimpleResult] = controller.contentByType("program").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram5), TVShortWithTimeZone(tvProgram6), TVShortWithTimeZone(tvProgram9))
    }

    "return NOT_FOUND by type notExist available today " in {
      val contentsResult: Future[SimpleResult] = controller.contentByType("notExist").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content for the type: notExist"))
    }

    "return the TV content for type SERIES in upper case available now" in {

      val contentsResult: Future[SimpleResult] = controller.currentContentByType("SERIES").apply(FakeRequest())
      status(contentsResult) mustBe (OK)
      contentType(contentsResult) mustBe (Some("application/json"))
      val contentInResponse = contentAsString(contentsResult)
      val tvcontents = Json.parse(contentInResponse).as[Seq[TVContentShort]]
      tvcontents mustEqual Seq(TVShortWithTimeZone(tvProgram7))
    }

    "return the TV content for type film in upper case available now" in {

      val contentsResult: Future[SimpleResult] = controller.currentContentByType("film").apply(FakeRequest())
      status(contentsResult) mustBe (OK)
      contentType(contentsResult) mustBe (Some("application/json"))
      val contentInResponse = contentAsString(contentsResult)
      val tvcontents = Json.parse(contentInResponse).as[Seq[TVContentShort]]
      tvcontents mustEqual Seq(TVShortWithTimeZone(tvProgram8))
    }

    "return NOT_FOUND by type notExist available now" in {
      val contentsResult: Future[SimpleResult] = controller.currentContentByType("notExist").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content at this moment for the type: notExist"))
    }

    "return the TV content for type program available from now until the end of the day" in {

      val programsResult: Future[SimpleResult] = controller.contentLeftByType("program").apply(FakeRequest())
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(TVShortWithTimeZone(tvProgram5), TVShortWithTimeZone(tvProgram6), TVShortWithTimeZone(tvProgram9))
    }

    "return NOT_FOUND by type notExist from now until the end of the day" in {
      val contentsResult: Future[SimpleResult] = controller.contentLeftByType("notExist").apply(FakeRequest())
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content left for the type: notExist"))
    }
  }
}

trait TVContentSetUpTest {

  val fakeNow = new DateTime(2014, 4, 4, 10, 0, 0, DateTimeZone.forID("UTC"))

  val tvProgram1 = TVContent("CHANNEL1", fakeNow.minusHours(3), fakeNow.minusHours(2), Some(List("program_type1", "ENTERTAINMENT")),
    Some(Series("serie1", Some("ep1"), None, None, None, None, None)),
    None,
    None,
    Some(BSONObjectID.generate))

  val tvProgram2 = TVContent("CHANNEL1", fakeNow.minusHours(2), fakeNow.minusHours(1), Some(List("program_type2", "SPORTS")),
    Some(Series("serie1", Some("ep1"), None, None, None, None, None)),
    None,
    None,
    Some(BSONObjectID.generate))

  val tvProgram3 = TVContent("CHANNEL1", fakeNow.minusHours(1), fakeNow.plusHours(1), Some(List("program_type3", "ENTERTAINMENT")),
    None,
    Some(Film("program1", None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram4 = TVContent("CHANNEL1", fakeNow.plusHours(1), fakeNow.plusHours(3), Some(List("program_type4", "SPORTS")),
    None,
    Some(Film("program1", None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram5 = TVContent("CHANNEL1", fakeNow.plusHours(3), fakeNow.plusHours(4), Some(List("HORROR", "program_type5")),
    None,
    None,
    Some(Program("p5", Some("d5"))),
    Some(BSONObjectID.generate))

  val tvProgram6 = TVContent("CHANNEL 3", fakeNow.plusHours(3), fakeNow.plusHours(5), Some(List("HORROR")),
    None,
    None,
    Some(Program("p6", Some("d6"))),
    Some(BSONObjectID.generate))

  val tvProgram7 = TVContent("CHANNEL4", fakeNow.minusHours(1), fakeNow.plusHours(2), Some(List("program_type3", "SPORTS")),
    Some(Series("serie1", Some("ep1"), None, None, None, None, None)),
    None,
    None,
    Some(BSONObjectID.generate))

  val tvProgram8 = TVContent("CHANNEL5", fakeNow.minusHours(1), fakeNow.plusHours(2), Some(List("program_type8", "SPORTS")),
    None,
    Some(Film("program1", None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram9 = TVContent("CHANNEL5", fakeNow.minusHours(4), fakeNow.plusHours(5), Some(List("program_type8", "HORROR")),
    None,
    None,
    Some(Program("p9", Some("d9"))),
    Some(BSONObjectID.generate))

  val tvContentRepository = new ContentRepository() {
    override def findLeftContentByChannel(channelName: String): Future[Seq[TVContent]] = {
      channelName match {
        case "CHANNEL1" => Future.successful(Seq(tvProgram3, tvProgram4, tvProgram5))
        case "CHANNEL3" => Future.successful(Seq(tvProgram6))
        case "CHANNEL4" => Future.successful(Seq(tvProgram7, tvProgram8))
        case "CHANNEL5" => Future.successful(Seq(tvProgram9))
        case _ => Future.successful(Seq())
      }
    }

    override def findDayContentByChannel(channelName: String): Future[Seq[TVContent]] = {
      channelName match {
        case "CHANNEL1" => Future.successful(Seq(tvProgram1, tvProgram2, tvProgram3, tvProgram4, tvProgram5))
        case "CHANNEL 3" => Future.successful(Seq(tvProgram6))
        case "CHANNEL4" => Future.successful(Seq(tvProgram7, tvProgram8))
        case "CHANNEL5" => Future.successful(Seq(tvProgram9))
        case _ => Future.successful(Seq())
      }
    }

    override def findCurrentContentByChannel(channelName: String): Future[Option[TVContent]] = {
      channelName match {
        case "CHANNEL1" => Future.successful(Some(tvProgram3))
        case "CHANNEL3" => Future.successful(Some(tvProgram6))
        case "CHANNEL4" => Future.successful(Some(tvProgram7))
        case "CHANNEL5" => Future.successful(Some(tvProgram9))
        case _ => Future.successful(None)
      }
    }

    override def findContentByID(contentID: String): Future[Option[TVContent]] = {
      if (contentID == tvProgram1.id.get.stringify)
        Future.successful(Some(tvProgram1))
      else Future.successful(None)
    }

    override def findDayContentByType(contentType: String): Future[Seq[TVContent]] = {
      contentType match {
        case "program" => Future.successful(
          Seq(tvProgram5, tvProgram6, tvProgram9))

        case "series" => Future.successful(Seq(tvProgram1, tvProgram2, tvProgram7))

        case "film" => Future.successful(Seq(tvProgram3, tvProgram4, tvProgram8))

        case _ => Future.successful(Seq())
      }
    }

    override def findCurrentContentByType(contentType: String): Future[Seq[TVContent]] = {
      contentType match {
        case "film" => Future.successful(Seq(tvProgram8))
        case "series" => Future.successful(Seq(tvProgram7))
        case _ => Future.successful(Seq())
      }
    }

    override def findLeftContentByType(contentType: String): Future[Seq[TVContent]] = {
      contentType match {
        case "program" => Future.successful(Seq(tvProgram5, tvProgram6, tvProgram9))
        case _ => Future.successful(Seq())
      }
    }
  }

  class App extends TVContentController {
    override val contentRepository = tvContentRepository
  }

  val controller = new App

}
