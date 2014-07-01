import sbt._
import templemore.sbt.cucumber.CucumberPlugin

object ApplicationBuild extends Build {

  val appName = "surfersTV"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.scalatest" % "scalatest_2.10.0-RC2" % "2.0.M5" % "test",
    "org.mongodb" %% "casbah" % "2.6.3",
    "com.novus" %% "salat" % "1.9.4"
  )

  val buildSettings = Defaults.defaultSettings ++ CucumberPlugin.cucumberSettingsWithTestPhaseIntegration

  val main = play.Project(appName, appVersion, appDependencies)



}