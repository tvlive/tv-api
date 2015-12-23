package models

import org.joda.time.DateTime

case class Authorization(username: String, dateExpired: DateTime)

case class Token(username: String, dateExpired: DateTime, token: String)

