package models

import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try


case class TVChannel(name: String, language: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

object TVChannel {


  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit object BSONObjectIDFormat extends Format[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = JsString(objectId.toString())
    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(x) => {
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(x)
        if(maybeOID.isSuccess) JsSuccess(maybeOID.get) else {
          JsError("Expected BSONObjectID as JsString")
        }
      }
      case _ => JsError("Expected BSONObjectID as JsString")
    }
  }

  implicit val reviewReads: Reads[TVChannel] = (
    (__ \ "name").read[String] and
      (__ \ "language").read[String] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVChannel.apply _)

  implicit val reviewWrites: Writes[TVChannel] = (
    (__ \ "name").write[String] and
      (__ \ "language").write[String] and
      (__ \ "id").write[Option[BSONObjectID]]
    )(unlift(TVChannel.unapply))


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



