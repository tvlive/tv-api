package controllers

import java.net.URLDecoder

import models._
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.Mockito._
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID
import utils.DomainBuilder.{TVProgramWithTimeZone, TVShortWithTimeZone}

import scala.concurrent.Future

class TVContentControllerSpec extends PlaySpec with MustMatchers {


  "TVContentController" should {

    "return NOT_FOUND if there is no TV content for CHANNEL2 available today" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByChannel("CHANNEL2")).thenReturn(Future.successful(Seq()))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.allContent("CHANNEL2").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content for the channel: CHANNEL2"))

      //AND
      verify(tvContentRepository).findDayContentByChannel("CHANNEL2")
    }

    "return all the TV content for a channel1 available today" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByChannel("CHANNEL1")).thenReturn(Future.successful(
        Seq(tvProgram1, tvProgram2, tvProgram3, tvProgram4, tvProgram5)))

      //WHEN
      val programResult: Future[SimpleResult] = controller.allContent("channel1").apply(FakeRequest())

      //THEN
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram1))
      tvprograms must contain(TVShortWithTimeZone(tvProgram2))
      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))

      //AND
      verify(tvContentRepository).findDayContentByChannel("CHANNEL1")

    }

    "return all the TV content for a CHANNEL 3 available today" in new TVContentSetUpTest() {
      //GIVEN
      val channel = URLDecoder.decode("CHANNEL+3", "UTF-8")
      when(tvContentRepository.findDayContentByChannel(channel)).thenReturn(Future.successful(
        Seq(tvProgram6)))

      //WHEN
      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL+3").apply(FakeRequest())

      //THEN
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram6))

      //AND
      verify(tvContentRepository).findDayContentByChannel(channel)
    }

    "return all the TV content for a CHANNEL1 available today" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByChannel("CHANNEL1")).thenReturn(Future.successful(
        Seq(tvProgram1,tvProgram2, tvProgram3, tvProgram4, tvProgram5 )))

      //WHEN
      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL1").apply(FakeRequest())

      //THEN
      status(programResult) mustBe (OK)
      contentType(programResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms must contain(TVShortWithTimeZone(tvProgram1))
      tvprograms must contain(TVShortWithTimeZone(tvProgram2))
      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))

      //AND
      verify(tvContentRepository).findDayContentByChannel("CHANNEL1")
    }

    "return NOT_FOUND if there is no TV content for CHANNEL2 available now" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findCurrentContentByChannel("CHANNEL2")).thenReturn(Future.successful(None))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.currentContent("CHANNEL2").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content at this moment for the channel: CHANNEL2"))

      //AND
      verify(tvContentRepository).findCurrentContentByChannel("CHANNEL2")
    }

    "return the TV content for CHANNEL1 available now" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findCurrentContentByChannel("CHANNEL1")).thenReturn(Future.successful(Some(tvProgram3)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.currentContent("CHANNEL1").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprogram = Json.parse(programInResponse).as[TVContentLong]
      tvprogram mustBe (TVProgramWithTimeZone(tvProgram3))

      //AND
      verify(tvContentRepository).findCurrentContentByChannel("CHANNEL1")

    }

    "return the TV content for CHANNEL1 available from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByChannel("CHANNEL1")).thenReturn(Future.successful(
        Seq(tvProgram3, tvProgram4, tvProgram5)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL1").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]

      tvprograms must contain(TVShortWithTimeZone(tvProgram3))
      tvprograms must contain(TVShortWithTimeZone(tvProgram4))
      tvprograms must contain(TVShortWithTimeZone(tvProgram5))

      //AND
      verify(tvContentRepository).findLeftContentByChannel("CHANNEL1")
    }


    "return NOT_FOUND if there is no TV content for CHANNEL2 available from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByChannel("CHANNEL2")).thenReturn(Future.successful(
        Seq()))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL2").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content left for the channel: CHANNEL2"))

      //AND
      verify(tvContentRepository).findLeftContentByChannel("CHANNEL2")
    }

    "return TV content details for a specific TV Content ID" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findContentByID(tvProgram1.id.get.stringify)).thenReturn(Future.successful(Some(tvProgram1)))

      //WHEN
      val programResult: Future[SimpleResult] = controller.tvContentDetails(tvProgram1.id.get.stringify).apply(FakeRequest())

      //THEN
      status(programResult) mustBe (OK)
      val programInResponse = contentAsString(programResult)
      val tvprogram = Json.parse(programInResponse).as[TVContentLong]
      tvprogram mustBe (TVProgramWithTimeZone(tvProgram1))

      //AND
      verify(tvContentRepository).findContentByID(tvProgram1.id.get.stringify)
    }

    "return NOT_FOUND if there is no TV content details for a specific TV Content ID" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findContentByID("noExistID")).thenReturn(Future.successful(None))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.tvContentDetails("noExistID").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content details with id: noExistID"))

      //AND
      verify(tvContentRepository).findContentByID("noExistID")
    }

    "return all the TV content available today by type SERIES and provider FREEVIEW with upper case" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByTypeAndProvider("series", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram1, tvProgram7)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.allContentByTypeAndProvider("SERIES", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram1), TVShortWithTimeZone(tvProgram7))

      //AND
      verify(tvContentRepository).findDayContentByTypeAndProvider("series", "FREEVIEW")
    }

    "return all the TV content available today by type film and provider freeview" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByTypeAndProvider("film", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram3, tvProgram4, tvProgram8)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.allContentByTypeAndProvider("film", "freeview").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram3), TVShortWithTimeZone(tvProgram4), TVShortWithTimeZone(tvProgram8))

      //AND
      verify(tvContentRepository).findDayContentByTypeAndProvider("film", "FREEVIEW")
    }

    "return all the TV content available today by type program and provider FREEVIEW" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByTypeAndProvider("program", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram5, tvProgram9)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.allContentByTypeAndProvider("program", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(
        TVShortWithTimeZone(tvProgram5), TVShortWithTimeZone(tvProgram9))

      //AND
      verify(tvContentRepository).findDayContentByTypeAndProvider("program", "FREEVIEW")
    }

   "return NOT_FOUND by provider notExist available today" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findDayContentByTypeAndProvider("program", "NOTEXIST")).thenReturn(
        Future.successful(Seq()))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.allContentByTypeAndProvider("program", "notExist").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content for type: program and provider: notExist"))

      //AND
      verify(tvContentRepository).findDayContentByTypeAndProvider("program", "NOTEXIST")
    }

    "return the TV content for type SERIES and provider FREEVIEW in upper case available now" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findCurrentContentByTypeAndProvider("series", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram7)))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.currentContentByTypeAndProvider("SERIES", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (OK)
      contentType(contentsResult) mustBe (Some("application/json"))
      val contentInResponse = contentAsString(contentsResult)
      val tvcontents = Json.parse(contentInResponse).as[Seq[TVContentShort]]
      tvcontents mustEqual Seq(TVShortWithTimeZone(tvProgram7))

      //AND
      verify(tvContentRepository).findCurrentContentByTypeAndProvider("series", "FREEVIEW")
    }

    "return the TV content for type film and provider freeview available now" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findCurrentContentByTypeAndProvider("film", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram8)))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.currentContentByTypeAndProvider("film", "freeview").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (OK)
      contentType(contentsResult) mustBe (Some("application/json"))
      val contentInResponse = contentAsString(contentsResult)
      val tvcontents = Json.parse(contentInResponse).as[Seq[TVContentShort]]
      tvcontents mustEqual Seq(TVShortWithTimeZone(tvProgram8))

      //AND
      verify(tvContentRepository).findCurrentContentByTypeAndProvider("film", "FREEVIEW")
    }

    "return NOT_FOUND by type notExist available now" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findCurrentContentByTypeAndProvider("notexist", "FREEVIEW")).thenReturn(
        Future.successful(Seq()))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.currentContentByTypeAndProvider("notExist", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content at this moment for the type: notExist and provider: FREEVIEW"))

      //AND
      verify(tvContentRepository).findCurrentContentByTypeAndProvider("notexist", "FREEVIEW")
    }

    "return the TV content for type PROGRAM and provider FREEVIEW available from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByTypeAndProvider("program", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram9)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.contentLeftByTypeAndProvider("PROGRAM", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(TVShortWithTimeZone(tvProgram9))

      //AND
      verify(tvContentRepository).findLeftContentByTypeAndProvider("program", "FREEVIEW")
    }

    "return the TV content for type series and provider freeview available from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByTypeAndProvider("series", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram7)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.contentLeftByTypeAndProvider("series", "freeview").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(TVShortWithTimeZone(tvProgram7))

      //AND
      verify(tvContentRepository).findLeftContentByTypeAndProvider("series", "FREEVIEW")
    }

    "return the TV content for type FILM and provider freeview available from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByTypeAndProvider("film", "FREEVIEW")).thenReturn(
        Future.successful(Seq(tvProgram8)))

      //WHEN
      val programsResult: Future[SimpleResult] = controller.contentLeftByTypeAndProvider("FILM", "freeview").apply(FakeRequest())

      //THEN
      status(programsResult) mustBe (OK)
      contentType(programsResult) mustBe (Some("application/json"))
      val programInResponse = contentAsString(programsResult)
      val tvprograms = Json.parse(programInResponse).as[Seq[TVContentShort]]
      tvprograms mustEqual Seq(TVShortWithTimeZone(tvProgram8))

      //AND
      verify(tvContentRepository).findLeftContentByTypeAndProvider("film", "FREEVIEW")
    }


    "return NOT_FOUND by type notExist from now until the end of the day" in new TVContentSetUpTest() {
      //GIVEN
      when(tvContentRepository.findLeftContentByTypeAndProvider("notexist", "FREEVIEW")).thenReturn(
        Future.successful(Seq()))

      //WHEN
      val contentsResult: Future[SimpleResult] = controller.contentLeftByTypeAndProvider("notExist", "FREEVIEW").apply(FakeRequest())

      //THEN
      status(contentsResult) mustBe (NOT_FOUND)
      val contentsInResponse = contentAsJson(contentsResult).as[NotFoundResponse]
      contentsInResponse mustEqual (NotFoundResponse(s"No TV content left for the type: notExist and provider: FREEVIEW"))

      //AND
      verify(tvContentRepository).findLeftContentByTypeAndProvider("notexist", "FREEVIEW")
    }
  }
}

trait TVContentSetUpTest extends MockitoSugar {

  val fakeNow = new DateTime(2014, 4, 4, 10, 0, 0, DateTimeZone.forID("UTC"))

  val tvProgram1 = TVContent("CHANNEL1", List("FREEVIEW", "SKY"), fakeNow.minusHours(3), fakeNow.minusHours(2),
    Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List(), List(), List(),  List(), List(), None,  None, None, None, None, None, None)), None, None,
    Some(BSONObjectID.generate))

  val tvProgram2 = TVContent("CHANNEL1", List("SKY"), fakeNow.minusHours(2), fakeNow.minusHours(1),
    Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List(), List(), List(),  List(), List(), None,  None, None, None, None, None, None)), None, None,
    Some(BSONObjectID.generate))

  val tvProgram3 = TVContent("CHANNEL1", List("FREEVIEW", "SKY"), fakeNow.minusHours(1), fakeNow.plusHours(1),
    None,
    Some(Film("program1", List(), List(), List(), List(),  List(), None, None, None, None, None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram4 = TVContent("CHANNEL1", List("FREEVIEW", "SKY"), fakeNow.plusHours(1), fakeNow.plusHours(3),
    None,
    Some(Film("program1", List(), List(), List(), List(),  List(), None, None, None, None, None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram5 = TVContent("CHANNEL1", List("FREEVIEW", "SKY"), fakeNow.plusHours(3), fakeNow.plusHours(4),
    None,
    None,
    Some(Program("p5", Some("d5"))),
    Some(BSONObjectID.generate))

  val tvProgram6 = TVContent("CHANNEL 3", List("SKY"), fakeNow.plusHours(3), fakeNow.plusHours(5),
    None,
    None,
    Some(Program("p6", Some("d6"))),
    Some(BSONObjectID.generate))

  val tvProgram7 = TVContent("CHANNEL4", List("FREEVIEW", "SKY"), fakeNow.minusHours(1), fakeNow.plusHours(2),
    Some(Series("serie1", Some(Episode(Some("ep1"), None, None, None, None)), List(), List(), List(), List(), List(), None, None, None, None, None, None, None)),
    None,
    None,
    Some(BSONObjectID.generate))

  val tvProgram8 = TVContent("CHANNEL5", List("FREEVIEW", "SKY"), fakeNow.minusHours(1), fakeNow.plusHours(2),
    None,
    Some(Film("program1", List(), List(), List(), List(),  List(), None, None, None, None,None, None, None)),
    None,
    Some(BSONObjectID.generate))

  val tvProgram9 = TVContent("CHANNEL5", List("FREEVIEW", "SKY"), fakeNow.minusHours(4), fakeNow.plusHours(5),
    None,
    None,
    Some(Program("p9", Some("d9"))),
    Some(BSONObjectID.generate))

  val tvContentRepository = mock[ContentRepository]

  class App extends TVContentController {
    override val contentRepository = tvContentRepository
  }

  val controller = new App

}
