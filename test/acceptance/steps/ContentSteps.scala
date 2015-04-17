package acceptance.steps

import java.util

import acceptance.support.Context._
import acceptance.support.{FilmBuilder, Http, ProgramBuilder, SeriesBuilder, _}
import controllers.{BadRequestResponse, NotFoundResponse}
import controllers.external.TVContentShort
import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import org.joda.time.format.DateTimeFormat
import org.scalatest.Matchers
import play.api.libs.json.Json

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class ContentSteps extends ScalaDsl with EN with Matchers with Http with Env {

  Given( """^the TV Provider "(.+)"$""") { (provider: String) =>
    world += "provider" -> provider.toUpperCase
  }

  Given( """^today is "(.+)"$""") { (now: String) =>
    world += "today" -> now
  }

  Given( """^the time is "(.+)"$""") { (now: String) =>
  }

  Given( """^the TV guide now for channel "(.+)" is:$""") { (channel: String, requestData: DataTable) =>
    requestData.asLists(classOf[String]).asScala.tail.foreach {
      e => insertContent(e.asScala.toList, channel)
    }
  }

  Given( """^the TV guide for the rest of the day is:$""") { (requestData: DataTable) =>
    requestData.asLists(classOf[String]).asScala.tail.foreach {
      e => insertContent(e.asScala.toList, e.get(10))
    }
  }

  When( """^I GET the resource "(.+)"$""") { (url: String) =>
    val (statusCode, content) = get(s"${host}$url")

    world += "statusCode" -> statusCode
    world += "content" -> content
  }

  Then( """^the HTTP response is "(.+)"$""") { (sc: String) =>
    world("statusCode") shouldBe statusCode(sc)
  }


  Then( """^the response is:$""") { (jsonExpected: String) =>
    world("statusCode") match {
      case "200" =>
        val contentsExpected = Json.parse(jsonExpected.stripMargin).as[Seq[TVContentShort]]
        val contents = Json.parse(world("content")).as[Seq[TVContentShort]]
        contentsExpected shouldBe contents
      case "404" =>
        val contentsExpected = Json.parse(jsonExpected.stripMargin).as[NotFoundResponse]
        val contents = Json.parse(world("content")).as[NotFoundResponse]
        contentsExpected shouldBe contents
      case "400" =>
        val contentsExpected = Json.parse(jsonExpected.stripMargin).as[BadRequestResponse]
        val contents = Json.parse(world("content")).as[BadRequestResponse]
        contentsExpected shouldBe contents
    }

  }

  Given( """^the TV guide now is:$""") { (requestData: DataTable) =>
    requestData.asLists(classOf[String]).asScala.tail.foreach {
      e => insertContent(e.asScala.toList, e.get(10))
    }
  }

  Then( """^the search contains the next content:$""") { (ce: DataTable) =>
    world("statusCode") shouldBe "200"

    val contents = Json.parse(world("content")).as[Seq[TVContentShort]]
    val contentExpected = ce.asLists(classOf[String]).asScala.tail
    contentExpected.size shouldBe contents.size
    val z = contentExpected.zip(contents)
    z.map {
      case (ce, c) =>
        val title = ce.get(1)
        ce.get(0) match {
          case "film" => c.film.get.title shouldBe title
          case "program" => c.program.get.title shouldBe title
          case "series" =>
            val st = c.series.map(_.serieTitle == title)
            val et = for {
              s <- c.series
              e <- s.episode
              et <- e.episodeTitle
            } yield et == title
            st.getOrElse(false) || et.getOrElse(false) shouldBe true
        }
    }

  }

  private def insertContent(content: List[String], channel: String) = {
    val d = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a")
    val start = d.parseDateTime(world("today") + " " + content(3))
    val end = d.parseDateTime(world("today") + " " + content(4))
    val id = content(0)
    val typeContent = content(1)
    val title = content(2)

    val tvc = typeContent match {
      case "film" =>
        FilmBuilder(channel, world("provider"), start, end, title, content(5).toDouble, content(6), id)
      case "program" =>
        ProgramBuilder(channel, world("provider"), start, end, title, id)
      case "series" =>
        SeriesBuilder(channel, world("provider"), start, end, title, content(7), content(8), content(9), content(5).toDouble,
          content(6), id)
      case _ => throw new IllegalArgumentException(s"Type ")
    }
    Try(db.insert(tvCollection, tvc)) match {
      case Success(i) => println(s"Inserting $tvc correctlry")
      case Failure(e) => println(s"Error inserting $tvc: ${e.getMessage}"); e
    }

  }
}
