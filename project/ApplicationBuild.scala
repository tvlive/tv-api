import sbt._
import templemore.sbt.cucumber.CucumberPlugin

object ApplicationBuild extends Build {

  val appName = "surfersTV"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.scalatest" % "scalatest_2.10.0-RC2" % "2.0.M5" % "test"
  )

  val buildSettings = Defaults.defaultSettings ++ CucumberPlugin.cucumberSettingsWithTestPhaseIntegration

  val main = play.Project(appName, appVersion, appDependencies)



}