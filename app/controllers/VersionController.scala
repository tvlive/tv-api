package controllers

import play.api.Play
import play.api.Play.current
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Version(versionNumber: String, status: String)

object VersionController extends VersionController

trait VersionController extends BaseController {

  def fetchVersion() = Action.async {

    Play.configuration.getString("app.version") match {
      case Some(version) if version.contains("SNAPSHOT") => Future(Ok(Json.toJson(Version(version, "UNSTABLE"))))
      case Some(version) => Future(Ok(Json.toJson(Version(version, "STABLE"))))
      case _ => Future(InternalServerError(Json.toJson(InternalErrorServerResponse("No version found"))))
    }
  }

}
