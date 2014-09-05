package models

import java.net.URLEncoder

import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class TVChannel(name: String, genre: String, language: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {
  val uriToday: String = controllers.routes.TVContentController.allContent(URLEncoder.encode(name, "UTF-8")).url
  val uriCurrent: String = controllers.routes.TVContentController.currentContent(URLEncoder.encode(name, "UTF-8")).url
  val uriLeft: String = controllers.routes.TVContentController.contentLeft(URLEncoder.encode(name,"UTF-8")).url
}

trait ChannelRepository {

  def listOfTVChannels(): Future[Seq[TVChannel]] = ???

}

class TVChannelRepository(name: String) extends ChannelRepository with Connection {
  override lazy val collectionName = name

  override def listOfTVChannels(): Future[Seq[TVChannel]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("name" -> 1),
      "$query" -> BSONDocument()
    )

    val found = collection.find(query).cursor[TVChannel]
    found.collect[Seq]()
  }
}

object TVChannelRepository {
  def apply(collectionName: String) = new TVChannelRepository(collectionName)
}



