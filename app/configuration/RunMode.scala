package configuration

import play.api.{Mode, Play}

trait RunMode {

  import play.api.Play.current

  lazy val env = {
    Play.mode match {
      case Mode.Test => "Test"
      case _ => Play.configuration.getString("run.mode").getOrElse("Dev")
    }
  }
}
