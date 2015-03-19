package configuration

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.Logger._
import play.api.Play._

trait Environment extends RunMode{
  info(s"loading the environment with run mode $env")
  val mongodbURI : String = configuration.getString(s"$env.mongodbURI").getOrElse(throw new IllegalArgumentException("mongodbURI is not defined"))
  val mongodbDatabaseName : String = configuration.getString(s"$env.mongodbDatabaseName").getOrElse(throw new IllegalArgumentException("mongodbDatabaseName is not defined"))
  val host : String = configuration.getString(s"$env.host").getOrElse(throw new IllegalArgumentException("host is not defined"))
  val time: Option[DateTime] =  configuration.getString(s"$env.time").map(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").parseDateTime(_))
}

object Environment extends Environment