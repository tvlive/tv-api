package acceptance.support

import acceptance.FeatureSuite
import com.github.tomakehurst.wiremock.client.WireMock
import cucumber.api.scala.{EN, ScalaDsl}

object Env extends ScalaDsl with EN {

  val db = Mongo("acceptance-tests")
  val tvCollection = db.createCollection("tvContent")

  val stubHost = "localhost"
  val stubPort = 1111
  val componentPost = 9000
  val host = s"http://$stubHost:$componentPost"

  Before {
    s =>
      db.removeCollection(tvCollection)
      println("data reset")
      FeatureSuite.ensureSetup
      Context.world.empty
  }

  After {
    s =>
      db.removeCollection(tvCollection)
      println("data reset")
      WireMock.reset()
  }

}

object Context {
  var world: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty[String, String]
}
