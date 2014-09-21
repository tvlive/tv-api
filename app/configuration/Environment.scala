package configuration

import play.api.Play

trait Environment {
  val mongodbURI : String = Play.current.configuration.getString("mongodbURI").getOrElse(throw new IllegalArgumentException("mongodbURI is not defined"))
  val mongodbDatabaseName : String = Play.current.configuration.getString("mongodbDatabaseName").getOrElse(throw new IllegalArgumentException("mongodbDatabaseName is not defined"))
}

object Environment extends Environment