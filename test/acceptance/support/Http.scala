package acceptance.support

import scala.util.{Failure, Success, Try}
import scalaj.http.{Http => HttpClient}

trait Http {

  val EmptyBody = ""
  val statusCode = Map(
    "OK" -> "200",
    "NOT FOUND" -> "404",
    "BAD REQUEST" -> "400",
    "CREATED" -> "201",
    "INTERNAL SERVER ERROR" -> "500"
  )

  def GET(url: String): (String, String) = {
    Try(HttpClient(url).asString) match {
      case Success(resp) => (resp.code.toString, resp.body)
      case Failure(ex) => println(s"Problem getting URL $url: ${ex.getMessage}"); throw ex
    }
  }

  def POST(url: String, bodyJson: String = EmptyBody): (String, String) = {
    Try(HttpClient(url).postData(bodyJson).header("content-type", "application/json").asString) match {
      case Success(resp) => (resp.code.toString, resp.body)
      case Failure(ex) => println(s"Problem post URL $url: ${ex.getMessage}"); throw ex
    }
  }
}

object Http extends Http
