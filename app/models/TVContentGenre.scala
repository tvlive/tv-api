package models

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVContentGenre(genre: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))


trait ContentGenreReporitory {
  def findAll(): Future[Seq[TVContentGenre]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVContentGenre]): Future[Int] = ???
}

class TVContentGenreRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ContentGenreReporitory {
  private val collection = con(collectionName).collection

  override def findAll(): Future[Seq[TVContentGenre]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("genre" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVContentGenre]
    found.collect[Seq]()
  }

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(enumerator: Enumerator[TVContentGenre]): Future[Int] =
    collection.bulkInsert(enumerator)
}
