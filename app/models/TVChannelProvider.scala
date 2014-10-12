package models

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class TVChannelProvider(provider: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

trait ChannelProviderRepository {

  def findAll(): Future[Seq[TVChannelProvider]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVChannelProvider]): Future[Int] = ???

}


class TVChannelProviderRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelProviderRepository {
  private val collection = con(collectionName).collection

  override def findAll(): Future[Seq[TVChannelProvider]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("provider" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVChannelProvider]
    found.collect[Seq]()
  }

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(enumerator: Enumerator[TVChannelProvider]): Future[Int] =
    collection.bulkInsert(enumerator)
}
