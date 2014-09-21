package configuration

import org.scalatestplus.play.PlaySpec
import play.api.test.{FakeApplication, WithApplication}

class EnvironmentSpec extends PlaySpec {


  "Environment" should {
    "be configured with all the values" in
      new WithApplication(FakeApplication(additionalConfiguration = Map("mongodbURI" -> "someURI", "mongodbDatabaseName" -> "someDatabaseName"))) {
        val env = new Environment(){}
        env.mongodbURI mustBe("someURI")
        env.mongodbDatabaseName mustBe("someDatabaseName")
    }

    "throw an IllegalArgumentException when no properyty mongodbURI defined" in
      new WithApplication(FakeApplication(additionalConfiguration = Map("mongodbDatabaseName" -> "someDatabaseName"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("mongodbURI is not defined")
      }

    "throw an IllegalArgumentException when no properyty mongodbDatabaseName defined" in
      new WithApplication(FakeApplication(additionalConfiguration = Map("mongodbURI" -> "someURI"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("mongodbDatabaseName is not defined")
      }
  }
}
