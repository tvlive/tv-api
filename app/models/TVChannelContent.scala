package models

import _root_.utils.TimeProvider
import org.joda.time.DateTime
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVContent(channel: String,
                     start: DateTime,
                     end: DateTime,
                     category: Option[List[String]],
                     series: Option[Series],
                     film: Option[Film],
                     id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class TVContentShort(channel: String,
                          start: DateTime,
                          end: DateTime,
                          category: Option[List[String]],
                          series: Option[SeriesShort],
                          film: Option[FilmShort],
                          id: Option[BSONObjectID] = Some(BSONObjectID.generate)) {

  val uriTVProgramDetails = controllers.routes.TVContentController.tvContentDetails(id.get.stringify).toString()
}


case class Series(serieTitle: String, episodeTitle: String, description: Option[String],
                  seasonNumber: Option[String], episodeNumber: Option[String], totalNumber: Option[String], actors: Option[List[String]])

case class Film(title: String, description: Option[String], actors: Option[List[String]], year: Option[String])

case class SeriesShort(serieTitle: String)

case class FilmShort(title: String)

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVContentShort]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVContentShort]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVContent]] = ???

  def findContentByID(contentID: String): Future[Option[TVContent]] = ???

  def findDayContentByGenre(genre: String): Future[Seq[TVContentShort]] = ???

  def findCurrentContentByGenre(genre: String): Future[Seq[TVContentShort]] = ???

  def findLeftContentByGenre(genre: String): Future[Seq[TVContentShort]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVContent]): Future[Int] = ???


}

class TVContentRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ContentRepository with TimeProvider {

  private val collection = con(collectionName).collection

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(channels: Enumerator[TVContent]): Future[Int] = collection.bulkInsert(channels)

  override def findDayContentByChannel(channelName: String): Future[Seq[TVContentShort]] = {

    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument("channel" -> channelName)
    )
    val found = collection.find(query).cursor[TVContentShort]
    found.collect[Seq]()
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVContent]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    collection.find(query).one[TVContent]
  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVContentShort]] = {

    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContentShort]
    found.collect[Seq]()
  }

  override def findContentByID(contentID: String): Future[Option[TVContent]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "_id" -> BSONObjectID(contentID)))

    collection.find(query).one[TVContent]
  }

  override def findDayContentByGenre(genre: String): Future[Seq[TVContentShort]] = {
    val query = BSONDocument(
      "$orderby" -> BSONDocument("channel" -> 1),
      "$query" -> BSONDocument("category" -> genre)
    )

    val found = collection.find(query).cursor[TVContentShort]
    found.collect[Seq]()
  }

  override def findCurrentContentByGenre(genre: String): Future[Seq[TVContentShort]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("channel" -> 1),
      "$query" -> BSONDocument(
        "category" -> genre,
        "start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContentShort]
    found.collect[Seq]()
  }

  override def findLeftContentByGenre(genre: String): Future[Seq[TVContentShort]] = {
    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("channel" -> 1),
      "$query" -> BSONDocument(
        "category" -> genre,
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContentShort]
    found.collect[Seq]()
  }
}



