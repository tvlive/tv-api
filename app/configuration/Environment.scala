package configuration

import play.api.Logger._
import play.api.Play

trait Environment {
  val mode = Play.current.mode.toString
  info(s"loading the environment with run mode $mode")
  val configuration = Play.current.configuration.getConfig(mode).getOrElse(throw new IllegalArgumentException(s"Execution mode $mode does not defined"))
  val mongodbURI : String = configuration.getString("mongodbURI").getOrElse(throw new IllegalArgumentException("mongodbURI is not defined"))
  val mongodbDatabaseName : String = configuration.getString("mongodbDatabaseName").getOrElse(throw new IllegalArgumentException("mongodbDatabaseName is not defined"))
}

object Environment extends Environment