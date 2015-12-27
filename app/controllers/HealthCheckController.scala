package controllers

import play.api.mvc.{Action, Controller}

object HealthCheckController extends Controller {

  def ping() = Action { Ok("pong") }

}
