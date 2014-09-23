package models

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

case class TVChannelGenre(genre: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))


trait ChannelGenreRepository {

  def findAll(): Future[Seq[TVChannelGenre]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVChannelGenre]): Future[Int] = ???

}


class TVChannelGenreRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelGenreRepository {
  private val collection = con(collectionName).collection

  override def findAll(): Future[Seq[TVChannelGenre]] = ???

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(enumerator: Enumerator[TVChannelGenre]): Future[Int] =
    collection.bulkInsert(enumerator)
}
