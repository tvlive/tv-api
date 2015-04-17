package acceptance.support

import scala.util.{Failure, Success, Try}
import scalaj.http.{Http => HttpClient}

trait Http {
  val statusCode = Map("OK" -> "200", "NOT FOUND" ->"404", "BAD REQUEST" -> "400")

  def get(url: String):(String, String) = {
    Try(HttpClient(url).asString) match {
      case Success(resp) => (resp.code.toString, resp.body)
      case Failure(ex) => println(s"Problem getting URL $url: ${ex.getMessage}");throw ex
    }

  }
}
