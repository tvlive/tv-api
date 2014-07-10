import sbt._
import scala.util.Properties._

object ApplicationBuild extends Build {

  val appName = "surfersTV"
  val appVersion = envOrElse("FREEAPI_VIEW_VERSION", "999-SNAPSHOT")

  val appDependencies = Seq(
    "org.reactivemongo" %% "reactivemongo" % "0.10.0",
    "org.scalatestplus" % "play_2.10" % "1.0.0" % "test"
  )

  val buildSettings = Defaults.defaultSettings

  val main = play.Project(appName, appVersion, appDependencies)


}