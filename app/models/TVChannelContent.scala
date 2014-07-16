package models

import _root_.utils.TimeProvider
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

case class TVProgram(channelName: String, programName: String, start: Long, end: Long, typeProgram: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

object TVProgram {

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

  implicit val reviewReads: Reads[TVProgram] = (
    (__ \ "channelName").read[String] and
    (__ \ "programName").read[String] and
      (__ \ "start").read[Long] and
      (__ \ "end").read[Long] and
      (__ \ "typeProgram").read[String] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVProgram.apply _)

  implicit val reviewWrites: Writes[TVProgram] = (
    (__ \ "channelName").write[String] and
      (__ \ "programName").write[String] and
      (__ \ "start").write[Long] and
      (__ \ "end").write[Long] and
      (__ \ "typeProgram").write[String] and
      (__ \ "id").write[Option[BSONObjectID]]
    )(unlift(TVProgram.unapply))


  implicit object TVProgramContentBSONReader extends BSONDocumentReader[TVProgram] {
    def read(doc: BSONDocument): TVProgram = {
      TVProgram(
        doc.getAs[BSONString]("channelName").get.value,
        doc.getAs[BSONString]("programName").get.value,
        doc.getAs[BSONLong]("start").get.value,
        doc.getAs[BSONLong]("end").get.value,
        doc.getAs[BSONString]("typeProgram").get.value,
        doc.getAs[BSONObjectID]("_id")
      )
    }
  }


  implicit object TVProgramContentBSONWriter extends BSONDocumentWriter[TVProgram] {
    override def write(t: TVProgram): BSONDocument = {
      BSONDocument(
        "_id" -> t.id.getOrElse(BSONObjectID.generate),
        "channelName" -> t.channelName,
        "programName" -> t.programName,
        "start" -> t.start,
        "end" -> t.end,
        "typeProgram" -> t.typeProgram
      )
    }
  }

}

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVProgram]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVProgram]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = ???

}

class TVContentRepository(name: String) extends ContentRepository with Connection with TimeProvider {

  override lazy val collectionName = name

  override def findDayContentByChannel(channelName: String): Future[Seq[TVProgram]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument("channelName" -> channelName)
    )
    val found = collection.find(query).cursor[TVProgram]
    found.collect[Seq]()
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channelName" -> channelName,
        "start" -> BSONDocument("$lte" -> currentDate()),
        "end" -> BSONDocument("$gte" -> currentDate())))

    collection.find(query).one[TVProgram]

  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVProgram]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument(
        "channelName" -> channelName,
        "end" -> BSONDocument("$gte" -> currentDate())))

    val found = collection.find(query).cursor[TVProgram]
    found.collect[Seq]()
  }
}

object TVContentRepository {
  def apply(collectionName: String) = new TVContentRepository(collectionName)
}



