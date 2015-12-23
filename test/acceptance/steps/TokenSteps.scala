package acceptance.steps

import acceptance.stubs.AuthStub._
import acceptance.support.Context._
import acceptance.support.Env._
import acceptance.support.Http
import acceptance.support.Http._
import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers

import scala.collection.JavaConverters._

class TokenSteps extends ScalaDsl with EN with Matchers {


  When( """^I POST the resource '(.*)' with:$""") { (tokenUrl: String, credential: DataTable) =>

    val cred = credential.asLists(classOf[String]).asScala.tail.head
    val username = cred.get(0)
    val dateExpired = cred.get(1)
    def tokenJson = s"""{"username":"$username","dateExpired":"$dateExpired"}"""
    tokenSuccessfully(tokenJson, username, dateExpired)

    val (status, body) = POST(s"${host}$tokenUrl", tokenJson)
    world = world += ("httpStatus" -> status)
    world = world += ("content" -> body)
  }

  When("""^I POST the resource '(.*)' with downstream '(.*)':$"""){ (tokenUrl: String, httpStatus: String, credential: DataTable) =>
    val cred = credential.asLists(classOf[String]).asScala.tail.head
    val username = cred.get(0)
    val dateExpired = cred.get(1)
    def tokenJson = s"""{"username":"$username","dateExpired":"$dateExpired"}"""
    tokenUnsuccessfully(Http.statusCode(httpStatus))

    val (status, body) = POST(s"${host}$tokenUrl", tokenJson)
    world = world += ("httpStatus" -> status)
    world = world += ("content" -> body)
  }

  Then( """^the HTTP status is '(.*)'$""") { (httpStatus: String) =>
    statusCode.get(httpStatus) shouldBe world.get("httpStatus")
  }


  When( """^I POST the resource '(.*)' with no body:$""") { (tokenUrl: String) =>
    val (status, body) = POST(s"${host}$tokenUrl")
    world = world += ("httpStatus" -> status)
    world = world += ("content" -> body)
  }
}
