package models

import _root_.utils.TimeProvider
import org.joda.time.DateTime
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVProgram(channel: String, start: DateTime, end: DateTime, category: Option[List[String]],
                     accessibility: Option[List[String]], series: Option[Serie], program: Option[Program], id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class TVProgramShort(channel: String, startTime: DateTime, endTime: DateTime, category: Option[List[String]], series: Option[SerieShort], program: Option[ProgramShort], id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {
  val uriTVProgramDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}


object TVShort {
  def apply(tvProgram: TVProgram): TVProgramShort = TVProgramShort(tvProgram.channel, tvProgram.start, tvProgram.end, tvProgram.category, tvProgram.series.map(s => SerieShort(s.serieTitle)),
    tvProgram.program.map(p => ProgramShort(p.title)), tvProgram.id)
}

case class Serie(serieTitle: String, episodeTitle: String, description: Option[String], seasonNumber: Option[String], episodeNumber: Option[String], totalNumber: Option[String])

case class Program(title: String, description: Option[String])

case class SerieShort(serieTitle: String)

case class ProgramShort(title: String)

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = ???

  def findContentByID(contentID: String): Future[Option[TVProgram]] = ???

  def findDayContentByGenre(genre: String): Future[Seq[TVProgramShort]] = ???

}

class TVContentRepository(name: String) extends ContentRepository with Connection with TimeProvider {

  override lazy val collectionName = name

  override def findDayContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = {

    val query = BSONDocument(
      "$orderby" -> BSONDocument("startTime" -> 1),
      "$query" -> BSONDocument("channel" -> channelName)
    )
    val found = collection.find(query).cursor[TVProgramShort]
    found.collect[Seq]()
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVProgram]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "startTime" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "endTime" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    collection.find(query).one[TVProgram]

  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVProgramShort]] = {

    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("startTime" -> 1),
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "endTime" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVProgramShort]
    found.collect[Seq]()
  }

  override def findContentByID(contentID: String): Future[Option[TVProgram]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "_id" -> BSONObjectID(contentID)))

    collection.find(query).one[TVProgram]
  }

  override def findDayContentByGenre(genre: String): Future[Seq[TVProgramShort]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("channel" -> 1),
      "$query" -> BSONDocument("category" -> genre)
        )

    val found = collection.find(query).cursor[TVProgramShort]
    found.collect[Seq]()
  }
}

object TVContentRepository {
  def apply(collectionName: String) = new TVContentRepository(collectionName)
}



