package utils

import models.APIMongoConnection

trait MongoSugar {

  implicit val conn: String => APIMongoConnection = {
    cn => new APIMongoConnection("localhost", "integration-tests") {
      override lazy val collectionName = cn
    }
  }
}
