package models

import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class TVChannel(id: Option[BSONObjectID], name: String, language: String)

object TVChannel {

  implicit object TVChannelFormat extends Format[TVChannel] {

    def writes(tvChannel: TVChannel): JsValue = {
      val tvChannelSeq = Seq(
        "name" -> JsString(tvChannel.name),
        "language" -> JsString(tvChannel.language)
      )
      JsObject(tvChannelSeq)
    }

    def reads(json: JsValue): JsResult[TVChannel] = {
      JsSuccess(TVChannel(Some(BSONObjectID.generate),"", ""))
    }
  }


  implicit object TVChannelBSONReader extends BSONDocumentReader[TVChannel] {
    def read(doc: BSONDocument): TVChannel = {
      TVChannel(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("language").get.value)
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

private [models] class TVChannelRepository(name: String) extends ChannelRepository with Connection {
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

object FakeTVChannelRepositoy extends ChannelRepository {
  override def listOfTVChannels(): Future[Seq[TVChannel]] = {
    Future {
      Seq(TVChannel(Some(BSONObjectID.generate),"Channel1", "EN"),
        TVChannel(Some(BSONObjectID.generate), "Channel2", "EN"),
        TVChannel(Some(BSONObjectID.generate), "Channel3", "EN"),
        TVChannel(Some(BSONObjectID.generate), "Channel4", "EN"))
    }
  }
}


