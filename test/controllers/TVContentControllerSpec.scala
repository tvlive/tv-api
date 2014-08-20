package controllers

import models.{Program, Serie, TVProgram, TVContentRepository}
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable.Specification
import org.specs2.runner._
import play.api.mvc.SimpleResult
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnitRunner])
class TVContentControllerSpec  extends Specification with TVContentSetUpTest {


  "TVContentController" should {

    "return empty all the TV content for CHANNEL2 available today" in {

      val programsResult: Future[SimpleResult] = controller.allContent("CHANNEL2").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }

    "return all the TV content for a channel1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("channel1").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgram]]
      println(programsInResponse)
      programsInResponse must contain(tvProgram1)
      programsInResponse must contain(tvProgram2)
      programsInResponse must contain(tvProgram3)
      programsInResponse must contain(tvProgram4)
      programsInResponse must contain(tvProgram5)
    }

    "return all the TV content for a CHANNEL 3 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL+3").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgram]]
      println(programsInResponse)
      programsInResponse must contain(tvProgram6)
    }


    "return all the TV content for a CHANNEL1 available today" in {

      val programResult: Future[SimpleResult] = controller.allContent("CHANNEL1").apply(FakeRequest())
      status(programResult) must equalTo(OK)
      contentType(programResult) must beSome.which(_ == "application/json")
      println(contentAsString(programResult))
      val programsInResponse = contentAsJson(programResult).as[Seq[TVProgram]]
      println(programsInResponse)
      programsInResponse must contain(tvProgram1)
      programsInResponse must contain(tvProgram2)
      programsInResponse must contain(tvProgram3)
      programsInResponse must contain(tvProgram4)
      programsInResponse must contain(tvProgram5)
    }
    //
    "return empty the TV content for CHANNEL2 available now" in {

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
      val programsInResponse = contentAsJson(programsResult).as[Seq[TVProgram]]
      programsInResponse must contain(tvProgram3)
      programsInResponse must contain(tvProgram4)
      programsInResponse must contain(tvProgram5)
    }

    "return empty the TV content for CHANNEL2 available from now until the end of the day" in {

      val programsResult: Future[SimpleResult] = controller.contentLeft("CHANNEL2").apply(FakeRequest())
      status(programsResult) must equalTo(NOT_FOUND)
    }
  }
}


trait TVContentSetUpTest {

  val fakeNow = new DateTime(2014, 4, 4, 10, 0, 0)
  val tvContentRepository = new TVContentRepository("tvContentTest") {
    override def currentDate() = fakeNow.toDate.getTime
  }
  val programs = tvContentRepository.collection
  programs.drop()
  Thread.sleep(5000)

  val tvProgram1 = TVProgram("CHANNEL1", "program1", fakeNow.minusHours(3).toDate.getTime, fakeNow.minusHours(2).toDate.getTime, Some("program_type1"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))
  val tvProgram2 = TVProgram("CHANNEL1", "program2", fakeNow.minusHours(2).toDate.getTime, fakeNow.minusHours(1).toDate.getTime, Some("program_type2"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))
  val tvProgram3 = TVProgram("CHANNEL1", "program3", fakeNow.minusHours(1).toDate.getTime, fakeNow.plusHours(1).toDate.getTime, Some("program_type3"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))
  val tvProgram4 = TVProgram("CHANNEL1", "program4", fakeNow.plusHours(1).toDate.getTime, fakeNow.plusHours(3).toDate.getTime, Some("program_type4"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))
  val tvProgram5 = TVProgram("CHANNEL1", "program5", fakeNow.plusHours(3).toDate.getTime, fakeNow.plusHours(4).toDate.getTime, Some("program_type5"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))
  val tvProgram6 = TVProgram("CHANNEL 3", "program5", fakeNow.plusHours(3).toDate.getTime, fakeNow.plusHours(5).toDate.getTime, Some("program_type5"),
    Some("flags1"), Serie("serie1", None, None, None, None), Program("program1", None), Some(BSONObjectID.generate))

  val p1 = programs.insert[TVProgram](tvProgram1)
  val p2 = programs.insert[TVProgram](tvProgram2)
  val p3 = programs.insert[TVProgram](tvProgram3)
  val p4 = programs.insert[TVProgram](tvProgram4)
  val p5 = programs.insert[TVProgram](tvProgram5)
  val p6 = programs.insert[TVProgram](tvProgram6)

  val resultPrograms = for {
    c1 <- p1
    c2 <- p2
    c3 <- p3
    c4 <- p4
    c5 <- p5
    c6 <- p6
  } yield (c1.ok && c2.ok && c3.ok && c4.ok && c5.ok && c6.ok)

  val isOkPrograms = Await.result(resultPrograms, Duration("20 seconds"))

  isOkPrograms match {
    case true => println("Elements inserted")
    case false => {
      programs.drop()
      throw new Exception("Error inserting elements")
    }
  }

  class App extends TVContentController{
    override val contentRepository: TVContentRepository = tvContentRepository
  }

  val controller = new App

}
