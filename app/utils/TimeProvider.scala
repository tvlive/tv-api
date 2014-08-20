package utils

import java.util.Date

trait TimeProvider {

  def currentDate() = new Date().getTime

}
