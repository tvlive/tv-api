package models

import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class TVChannel(name: String, language: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

object TVChannel {

  implicit object TVChannelBSONReader extends BSONDocumentReader[TVChannel] {
    def read(doc: BSONDocument): TVChannel = {
      TVChannel(
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("language").get.value,
        doc.getAs[BSONObjectID]("_id"))
    }
  }

  implicit object TVChannelBSONWriter extends BSONDocumentWriter[TVChannel] {
    override def write(t: TVChannel): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "name" -> t.name,
        "language" -> t.language
      )
    }
  }

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



