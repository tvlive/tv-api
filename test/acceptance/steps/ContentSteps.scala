package acceptance.steps

import acceptance.support.Context._
import acceptance.support.{FilmBuilder, Http, ProgramBuilder, SeriesBuilder, _}
import controllers.NotFoundResponse
import controllers.external.TVContentShort
import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import org.joda.time.format.DateTimeFormat
import org.scalatest.Matchers
import play.api.libs.json.Json

import scala.collection.JavaConverters._

class ContentSteps extends ScalaDsl with EN with Matchers with Http with Env {

  Given( """^the TV Provider "(.+)"$""") { (provider: String) =>
    world += "provider" -> provider.toUpperCase
  }

  Given( """^today is "(.+)"$""") { (now: String) =>
    world += "today" -> now
  }

  Given( """^the TV guide now for channel "(.+)" is:$""") { (channel: String, requestData: DataTable) =>
    requestData.asLists(classOf[String]).asScala.tail.foreach {
      e => insertContent(e.asScala.toList, channel)
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
        contents shouldBe contentsExpected
      case "404" =>
        val contentsExpected = Json.parse(jsonExpected.stripMargin).as[NotFoundResponse]
        val contents = Json.parse(world("content")).as[NotFoundResponse]
        contents shouldBe contentsExpected
    }

  }

  Given( """^the TV guide now is:$""") { (requestData: DataTable) =>
    requestData.asLists(classOf[String]).asScala.tail.foreach {
      e => insertContent(e.asScala.toList, e.get(10))
    }
  }

  private def insertContent(content: List[String], channel: String) = {
    val d = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm a")
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
      case "series" => SeriesBuilder(channel, world("provider"), start, end, title, content(7), content(8), content(9), content(5).toDouble,
        content(6), id)
      case _ => throw new IllegalArgumentException(s"Type ")
    }
    println("inserting data for scenario")
    db.insert(tvCollection, tvc)
  }
}
