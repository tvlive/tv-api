import sbt._
import Keys._
import scala.util.Properties._

object ApplicationBuild extends Build {

  val appName = "surfersTV"
  val appVersion = envOrElse("TV_API_VIEW_VERSION", "999-SNAPSHOT")

  def unitFilter(name: String): Boolean = !integrationFilter(name) && !acceptanceFilter(name)
  def acceptanceFilter(name: String): Boolean = name contains "FeaturesRunner"
  def integrationFilter(name: String): Boolean = name contains "Int"

  lazy val IntTest = config("integration") extend(Test)
  lazy val AccTest = config("acceptance") extend (Test)

  val appDependencies = Seq(
    "org.mockito" % "mockito-core" % "1.9.5",
    "org.reactivemongo" %% "reactivemongo" % "0.10.0"
  )

  val buildSettings = Defaults.defaultSettings

  val main = play.Project(appName, appVersion, appDependencies)
    .settings(scalaSource in Test <<= baseDirectory(_ / "test/unit"),
      testOptions in Test := Seq(Tests.Filter(unitFilter)))

    .configs( IntTest )
    .settings(inConfig(IntTest)(Defaults.testSettings) : _*)
    .settings(scalaSource in Test <<= baseDirectory(_ / "test/unit"),
      testOptions in Test := Seq(Tests.Filter(unitFilter)),
      testOptions in IntTest := Seq(Tests.Filter(integrationFilter)))
    .settings(scalaSource in IntTest <<= baseDirectory(_ / "test/integration"))
    .settings(libraryDependencies ++= Seq("org.scalatestplus" % "play_2.10" % "1.0.0" % "test"))
    .settings(Keys.fork in (Test) := false)

    .configs(AccTest)
    .settings(testOptions in AccTest := Seq(Tests.Filter(acceptanceFilter)))
    .settings(inConfig(AccTest)(Defaults.testSettings): _*)
    .settings(scalaSource in AccTest <<= baseDirectory(_ / "test/acceptance"))
    .settings(libraryDependencies ++= Seq("org.scalatestplus" % "play_2.10" % "1.0.0" % "test",
    "info.cukes" %% "cucumber-scala" % "1.2.2" % "test",
    "info.cukes" % "cucumber-junit" % "1.2.2" % "test",
    "junit" % "junit" % "4.11" % "test",
    "org.mongodb" %% "casbah" % "2.7.3" % "test",
    "org.scalaj" %% "scalaj-http" % "1.1.4" % "test",
    "com.github.tomakehurst" % "wiremock" % "2.0.8-beta" % "test"
  ))

}