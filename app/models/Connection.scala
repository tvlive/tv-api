package models

import configuration.Environment
import reactivemongo.api.collections.default.BSONCollection

class APIMongoConnection(uri: String = Environment.mongodbURI,
                      databaseName: String = Environment.mongodbDatabaseName) {

  import reactivemongo.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val driver = new MongoDriver
  val connection = driver.connection(List(uri))
  val db = connection(databaseName)
  lazy val collectionName: String = ???
  lazy val collection: BSONCollection = db(collectionName)
}

