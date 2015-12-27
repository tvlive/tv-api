package infrastructure

import models.AuthorizationInfo
import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Success}

class DecryptorSpec extends WordSpec with Matchers {
  val encoded = new sun.misc.BASE64Encoder().encode("a.v@tvlive.io:0123456789".getBytes)

  "decodeToken" should {
    "return a.v@tvlive.io and 0123456789" in {
      Decryptor.decodeToken(encoded) shouldBe Success(AuthorizationInfo("a.v@tvlive.io", "0123456789"))
    }

    "throws an AuthorizationIncorrectException when the token encoded is not well formatted" in {
      Decryptor.decodeToken("asdfghjkl2345678") shouldBe Failure(InvalidTokenException)
    }
  }

  "encodeToken" should {
    "return a username and token encoded" in {
      Decryptor.encode("a.v@tvlive.io", "0123456789") shouldBe "YS52QHR2bGl2ZS5pbzowMTIzNDU2Nzg5"
    }
  }
}
