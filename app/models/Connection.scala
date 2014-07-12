package models

import reactivemongo.api.collections.default.BSONCollection

trait Connection {

    import reactivemongo.api._

import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    val db = connection("alvaro_tests")

    lazy val collectionName: String = ???
    lazy val collection: BSONCollection =  db(collectionName)

}
