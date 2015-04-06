package models

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class TVChannel(name: String, provider: List[String], category: List[String], id: Option[BSONObjectID] = Some(BSONObjectID.generate))

trait ChannelRepository {

  def listOfTVChannels(): Future[Seq[TVChannel]] = ???

  def listOfTVChannelsByCategory(category: String): Future[Seq[TVChannel]] = ???

  def listOfTVChannelsByProvider(provider: String): Future[Seq[TVChannel]] = ???

  def removeAll(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVChannel]): Future[Int] = ???
}

class TVChannelRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelRepository{

  private val collection = con(collectionName).collection

  override def removeAll(): Future[Boolean] = collection.remove(BSONDocument()).map(_.ok)

  override def insertBulk(channels: Enumerator[TVChannel]): Future[Int] = collection.bulkInsert(channels)

  override def listOfTVChannels(): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }

  override def listOfTVChannelsByCategory(category: String): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument("category" -> category)
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }

 override def listOfTVChannelsByProvider(provider: String): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument("provider" -> provider)
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }

}



