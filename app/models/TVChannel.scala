package models

import java.net.URLEncoder

import play.api.libs.iteratee.Enumerator
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class TVChannel(name: String, provider: List[String], id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {
  val uriToday: String = controllers.routes.TVContentController.allContent(URLEncoder.encode(name, "UTF-8")).url
  val uriCurrent: String = controllers.routes.TVContentController.currentContent(URLEncoder.encode(name, "UTF-8")).url
  val uriLeft: String = controllers.routes.TVContentController.contentLeft(URLEncoder.encode(name,"UTF-8")).url
}

trait ChannelRepository {

  def listOfTVChannels(): Future[Seq[TVChannel]] = ???
  def listOfTVChannelsByGenre(genre: String): Future[Seq[TVChannel]] = ???
  def drop(): Future[Boolean] = ???
  def insertBulk(enumerator: Enumerator[TVChannel]): Future[Int] = ???
}

class TVChannelRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ChannelRepository{

  private val collection = con(collectionName).collection

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(channels: Enumerator[TVChannel]): Future[Int] = collection.bulkInsert(channels)

  override def listOfTVChannels(): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }

  override def listOfTVChannelsByGenre(genre: String): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument("genre" -> genre)
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }

}



