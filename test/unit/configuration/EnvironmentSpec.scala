package configuration

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatestplus.play.PlaySpec
import play.api.test.{FakeApplication, WithApplication}

import scala.util.{Failure, Try}

class EnvironmentSpec extends PlaySpec {


  "Environment" should {
    "be configured with all the values" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map("Test.mongodbURI" -> "someURI",
          "Test.mongodbDatabaseName" -> "someDatabaseName",
          "Test.host" -> "http://localhost:9000",
          "Test.time" -> "30/05/2010 22:15:52"
        ))) {
        val env = new Environment(){}
        env.mongodbURI mustBe("someURI")
        env.mongodbDatabaseName mustBe("someDatabaseName")
        env.host mustBe("http://localhost:9000")
        env.time mustBe(Some(new DateTime(2010,5,30,22,15,52)))
    }

    "be configured with all the values but time is None" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map("Test.mongodbURI" -> "someURI",
          "Test.mongodbDatabaseName" -> "someDatabaseName",
          "Test.host" -> "http://localhost:9000"))) {
        val env = new Environment(){}
        env.mongodbURI mustBe("someURI")
        env.mongodbDatabaseName mustBe("someDatabaseName")
        env.host mustBe("http://localhost:9000")
        env.time mustBe(None)
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

    "throw an IllegalArgumentException when no property host defined" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map("Test.mongodbURI" -> "someURI", "Test.mongodbDatabaseName" -> "someDatabase"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage mustBe("host is not defined")
      }

    "throw an IllegalArgumentException when property time is bad formed" in
      new WithApplication(FakeApplication(additionalConfiguration =
        Map(
          "Test.mongodbURI" -> "someURI",
          "Test.mongodbDatabaseName" -> "someDatabase",
          "Test.host" -> "someHost",
          "Test.time" -> "someBadFormattedTime"))) {
        val ex = intercept[IllegalArgumentException] {
          val env = new Environment(){}
        }
        ex.getMessage must include("someBadFormattedTime")
      }
  }
}
