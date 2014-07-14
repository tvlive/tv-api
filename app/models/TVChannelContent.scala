package models

import _root_.utils.TimeProvider
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVProgram(channelName: String, programName: String, start: Long, end: Long, typeProgram: String, id: Option[BSONObjectID] = Some(BSONObjectID.generate))


object TVProgram {

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



