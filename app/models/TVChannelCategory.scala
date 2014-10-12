package models

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

case class TVChannelCategory(category: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

trait ChannelCategoryRepository {

  def findAll(): Future[Seq[TVChannelCategory]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVChannelCategory]): Future[Int] = ???

}


class TVChannelCategoryRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelCategoryRepository {
  private val collection = con(collectionName).collection

  override def findAll(): Future[Seq[TVChannelCategory]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("category" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVChannelCategory]
    found.collect[Seq]()
  }

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(enumerator: Enumerator[TVChannelCategory]): Future[Int] =
    collection.bulkInsert(enumerator)
}
