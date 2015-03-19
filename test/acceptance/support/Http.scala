package acceptance.support

import scalaj.http.{Http => HttpClient}

trait Http {
  val statusCode = Map("OK" -> "200", "NOT FOUND" ->"404")

  def get(url: String) = {
    val resp = HttpClient(url).asString
    (resp.code.toString, resp.body)
  }
}
