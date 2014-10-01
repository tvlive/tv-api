import sbt._
import Keys._
import scala.util.Properties._

object ApplicationBuild extends Build {

  val appName = "surfersTV"
  val appVersion = envOrElse("TV_API_VIEW_VERSION", "999-SNAPSHOT")

  lazy val IntTest = config("integration") extend(Test)

  val appDependencies = Seq(
    "org.reactivemongo" %% "reactivemongo" % "0.10.0"
  )

  val buildSettings = Defaults.defaultSettings

  val main = play.Project(appName, appVersion, appDependencies)
    .configs( IntTest )
    .settings(inConfig(IntTest)(Defaults.testSettings) : _*)
    .settings(scalaSource in Test <<= baseDirectory(_ / "test/unit"))
    .settings(scalaSource in IntTest <<= baseDirectory(_ / "test/integration"))
    .settings(libraryDependencies ++= Seq("org.scalatestplus" % "play_2.10" % "1.0.0" % "test"))
    .settings(Keys.fork in (Test) := false)


}