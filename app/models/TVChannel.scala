package models

import utils.mongoBuilder._
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

  def drop(): Future[Boolean] = ???
}

class TVChannelRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelRepository{

  private val collection = con(collectionName).collection

  override def removeAll(): Future[Boolean] = collection.remove(BSONDocument()).map(_.ok)

  override def insertBulk(channels: Enumerator[TVChannel]): Future[Int] = collection.bulkInsert(channels)

  override def listOfTVChannels(): Future[Seq[TVChannel]] = {
    collection
      .find(BSONDocument())
      .sort(BSONDocument().nameAsc())
      .cursor[TVChannel]
      .collect[Seq]()
  }

  override def listOfTVChannelsByCategory(category: String): Future[Seq[TVChannel]] = {
    collection
      .find(BSONDocument().category(category))
      .sort(BSONDocument().nameAsc())
      .cursor[TVChannel]
      .collect[Seq]()
  }

 override def listOfTVChannelsByProvider(provider: String): Future[Seq[TVChannel]] = {
    collection
      .find(BSONDocument().provider(provider))
      .sort(BSONDocument().nameAsc())
      .cursor[TVChannel]
      .collect[Seq]()
  }

  override def drop(): Future[Boolean] = collection.drop()
}



