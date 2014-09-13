package controllers

import models._
import org.joda.time.DateTime
import org.junit.runner._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.libs.iteratee.Enumerator
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class TVContentControllerSpec extends Specification with TVContentSetUpTest {


  "TVContentController" should {

    "return NOT_FOUND if there is no TV content for CHANNEL2 available today" in {

      val programsResult: Future[SimpleResult] = controller.allContent("CHANNEL2").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }

    "return all the TV content for a channel1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("channel1").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgramShort]]
      programsInResponse must contain(TVShort(tvProgram1))
      programsInResponse must contain(TVShort(tvProgram2))
      programsInResponse must contain(TVShort(tvProgram3))
      programsInResponse must contain(TVShort(tvProgram4))
      programsInResponse must contain(TVShort(tvProgram5))

    }

    "return all the TV content for a CHANNEL 3 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL+3").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgramShort]]
      println(programsInResponse)
      programsInResponse must contain(TVShort(tvProgram6))
    }


    "return all the TV content for a CHANNEL1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL1").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgramShort]]
      programsInResponse must contain(TVShort(tvProgram1))
      programsInResponse must contain(TVShort(tvProgram2))
      programsInResponse must contain(TVShort(tvProgram3))
      programsInResponse must contain(TVShort(tvProgram4))
      programsInResponse must contain(TVShort(tvProgram5))
    }
    //
    "return NOT_FOUND if there is no TV content for CHANNEL2 available now" in {

      val programsResult: Future[SimpleResult] = controller.currentContent("CHANNEL2").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }

    "return the TV content for CHANNEL1 available now" in {

      val programsResult: Future[SimpleResult] = controller.currentContent("CHANNEL1").apply(FakeRequest())
      status(programsResult) must equalTo(OK)
      contentType(programsResult) must beSome.which(_ == "application/json")
      val programsInResponse = contentAsJson(programsResult).as[TVProgram]
      programsInResponse must be_==(tvProgram3)
    }

    "return the TV content for CHANNEL1 available from now until the end of the day" in {

      val programsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL1").apply(FakeRequest())
      status(programsResult) must equalTo(OK)
      contentType(programsResult) must beSome.which(_ == "application/json")
      val programsInResponse = contentAsJson(programsResult).as[Seq[TVProgramShort]]
      programsInResponse must contain(TVShort(tvProgram3))
      programsInResponse must contain(TVShort(tvProgram4))
      programsInResponse must contain(TVShort(tvProgram5))

    }

    "return NOT_FOUND if there is no TV content for CHANNEL2 available from now until the end of the day" in {

      val programsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL2").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }

    "return TV content details for a specific TV Content ID" in {

      val programResult: Future[SimpleResult] = controller.tvContentDetails(tvProgram1.id.get.stringify).apply(FakeRequest())
      status(programResult) must equalTo(OK)
      val programInResponse = contentAsJson(programResult).as[TVProgram]
      programInResponse must be_==(tvProgram1)
    }

    "return NOT_FOUND if there is no TV content details for a specific TV Content ID" in {

      val programResult: Future[SimpleResult] = controller.tvContentDetails(BSONObjectID.generate.stringify).apply(FakeRequest())
      status(programResult) must equalTo(NOT_FOUND)
    }

    "return all the TV content available today by genre ENTERTAINMENT with upper case" in {
      val programResult: Future[SimpleResult] = controller.contentByGenre("entertainment").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      val programInResponse = contentAsJson(programResult).as[Seq[TVProgramShort]]
      programInResponse mustEqual Seq(TVShort(tvProgram1),TVShort(tvProgram3))

    }

    "return all the TV content available today by genre sports with lower case" in {
      val programResult: Future[SimpleResult] = controller.contentByGenre("sports").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      val programInResponse = contentAsJson(programResult).as[Seq[TVProgramShort]]
      programInResponse mustEqual Seq(TVShort(tvProgram2),TVShort(tvProgram4), TVShort(tvProgram7), TVShort(tvProgram8))

    }

    "return NOT_FOUND if there is no TV content for genre FILM available now" in {

      val programsResult: Future[SimpleResult] = controller.currentContentByGenre("FILM").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }

    "return the TV content for genre SPORTS available now" in {

      val programsResult: Future[SimpleResult] = controller.currentContentByGenre("SPORTS").apply(FakeRequest())
      status(programsResult) must equalTo(OK)
      contentType(programsResult) must beSome.which(_ == "application/json")
      val programsInResponse = contentAsJson(programsResult).as[Seq[TVProgramShort]]
      programsInResponse mustEqual Seq(TVShort(tvProgram7), TVShort(tvProgram8))
    }
  }
}

trait TVContentSetUpTest extends ScalaFutures {

  self:Specification =>

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(5, Millis))

  val fakeNow = new DateTime(2014, 4, 4, 10, 0, 0)
  val tvContentRepository = new TVContentRepository("tvContentTest") {
    override def currentDate() = fakeNow
  }
  val programs = tvContentRepository.collection
  programs.drop()
  Thread.sleep(5000)

  val tvProgram1 = TVProgram("CHANNEL1", fakeNow.minusHours(3), fakeNow.minusHours(2), Some(List("program_type1", "ENTERTAINMENT")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram2 = TVProgram("CHANNEL1", fakeNow.minusHours(2), fakeNow.minusHours(1), Some(List("program_type2", "SPORTS")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram3 = TVProgram("CHANNEL1", fakeNow.minusHours(1), fakeNow.plusHours(1), Some(List("program_type3", "ENTERTAINMENT")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram4 = TVProgram("CHANNEL1", fakeNow.plusHours(1), fakeNow.plusHours(3), Some(List("program_type4", "SPORTS")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram5 = TVProgram("CHANNEL1", fakeNow.plusHours(3), fakeNow.plusHours(4), Some(List("program_type5")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram6 = TVProgram("CHANNEL 3", fakeNow.plusHours(3), fakeNow.plusHours(5), Some(List("program_type5")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram7 = TVProgram("CHANNEL4", fakeNow.minusHours(1), fakeNow.plusHours(2), Some(List("program_type3", "SPORTS")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))
  val tvProgram8 = TVProgram("CHANNEL5", fakeNow.minusHours(1), fakeNow.plusHours(2), Some(List("program_type8", "SPORTS")),
    Some(List("flags1")), Some(Serie("serie1", "ep1", None, None, None, None)), Some(Program("program1", None)), Some(BSONObjectID.generate))

  whenReady(programs.bulkInsert(Enumerator(tvProgram1, tvProgram2, tvProgram3,
    tvProgram4, tvProgram5, tvProgram6, tvProgram7, tvProgram8))) {
    response => response must_== 8
  }

  class App extends TVContentController {
    override val contentRepository: TVContentRepository = tvContentRepository
  }

  val controller = new App

}
