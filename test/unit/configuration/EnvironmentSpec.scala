package configuration

import org.scalatestplus.play.PlaySpec
import play.api.test.{FakeApplication, WithApplication}

class EnvironmentSpec extends PlaySpec {


  "Environment" should {
    "be configured with all the values" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map("Test.mongodbURI" -> "someURI",
          "Test.mongodbDatabaseName" -> "someDatabaseName",
          "Test.host" -> "http://localhost:9000"))) {
        val env = new Environment(){}
        env.mongodbURI mustBe("someURI")
        env.mongodbDatabaseName mustBe("someDatabaseName")
        env.host mustBe("http://localhost:9000")
    }

    "throw an IllegalArgumentException when no properyty mongodbURI defined" in
      new WithApplication(FakeApplication(additionalConfiguration = Map("Test.something" -> "something"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("mongodbURI is not defined")
      }

    "throw an IllegalArgumentException when no properyty mongodbDatabaseName defined" in
      new WithApplication(FakeApplication(additionalConfiguration = Map("Test.mongodbURI" -> "someURI"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("mongodbDatabaseName is not defined")
      }

    "throw an IllegalArgumentException when no properyty host defined" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map("Test.mongodbURI" -> "someURI", "Test.mongodbDatabaseName" -> "someDatabase"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("host is not defined")
      }
  }
}
