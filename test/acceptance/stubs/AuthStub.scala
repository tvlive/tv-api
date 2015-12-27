package acceptance.stubs

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._

object AuthStub {

  def tokenSuccessfully(tokenJson: String, username: String, dateExpired: String) = {
    stubFor(post(urlEqualTo("/authorize"))
      .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
      .withRequestBody(WireMock.equalToJson(tokenJson))
      .willReturn(aResponse()
      .withStatus(201)
      .withBody( s"""{"username":"$username","dateExpired":"$dateExpired","token":"12345678900987654321"}""")))
  }

  def tokenUnsuccessfully(status: String) = {
    stubFor(post(urlEqualTo("/authorize"))
      .willReturn(aResponse()
      .withStatus(status.toInt)))
  }

  def findToken(username: String, token: String, status: Int = 200) = {
    stubFor(get(urlEqualTo(s"/authorize/$username/$token"))
      .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
      .willReturn(aResponse()
      .withStatus(status)))
  }

}
