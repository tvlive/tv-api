package acceptance.support

import cucumber.api.scala.ScalaDsl

trait Env extends ScalaDsl {

  val db = Mongo("acceptance-tests")
  val tvCollection = db.createCollection("tvContent")

  val host = "http://localhost:9000"

  Before {
    s =>
      db.removeCollection(tvCollection)
      println("data reset")
      Context.world.empty
  }

  After {
    s =>
      db.removeCollection(tvCollection)
      println("data reset")
  }

}

object Context {
  var world: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty[String, String]
}