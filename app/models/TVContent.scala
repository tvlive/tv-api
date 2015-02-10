package models

import utils.TimeProvider
import org.joda.time.{Duration, DateTime}
import play.api.libs.iteratee.Enumerator
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVContent(channel: String,
                     provider: List[String],
                     start: DateTime,
                     end: DateTime,
                     series: Option[Series],
                     film: Option[Film],
                     program: Option[Program],
                     id: Option[BSONObjectID] = Some(BSONObjectID.generate)) extends TimeProvider with ChannelImageURLBuilder {

  val onTimeNow = (start.isBeforeNow || start.isEqualNow) && (end.isAfterNow || end.isEqualNow)

  val perCentTimeElapsed: Option[Long] = {
    onTimeNow match {
      case true => {
        val initialDuration = new Duration(start, end).getStandardMinutes
        val currentDuration = new Duration(start, currentDate).getStandardMinutes
        Some(currentDuration * 100 / initialDuration)
      }
      case false => None
    }
  }

  val channelImageURL = buildUrl(channel)
}


case class Series(serieTitle: String,
                  episode: Option[Episode],
                  actors: List[String],
                  writer: List[String],
                  director: List[String],
                  genre: List[String],
                  country: List[String],
                  language: Option[String],
                  rating: Option[Double],
                  awards: Option[String],
                  poster: Option[String],
                  plot: Option[String],
                  year: Option[String],
                  imdbId: Option[String])

case class Episode(episodeTitle: Option[String],
                   episodePlot: Option[String],
                   seasonNumber: Option[String],
                   episodeNumber: Option[String],
                   totalNumber: Option[String])

case class Film(title: String,
                actors: List[String],
                writer: List[String],
                director: List[String],
                genre: List[String],
                country: List[String],
                language: Option[String],
                rating: Option[Double],
                awards: Option[String],
                poster: Option[String],
                plot: Option[String],
                year: Option[String],
                imdbId: Option[String])


case class Program(title: String, plot: Option[String])

trait ContentRepository {

  def findLeftContentByChannel(channelName: String): Future[Seq[TVContent]] = ???

  def findDayContentByChannel(channelName: String): Future[Seq[TVContent]] = ???

  def findCurrentContentByChannel(channelName: String): Future[Option[TVContent]] = ???

  def findContentByID(contentID: String): Future[Option[TVContent]] = ???

  def findDayContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = ???

  def findCurrentContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = ???

  def findLeftContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = ???

  def drop(): Future[Boolean] = ???

  def insertBulk(enumerator: Enumerator[TVContent]): Future[Int] = ???

}

class TVContentRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends ContentRepository with TimeProvider {

  private val collection = con(collectionName).collection

  override def drop(): Future[Boolean] = collection.drop()

  override def insertBulk(channels: Enumerator[TVContent]): Future[Int] = collection.bulkInsert(channels)

  override def findDayContentByChannel(channelName: String): Future[Seq[TVContent]] = {

    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument("channel" -> channelName)
    )
    val found = collection.find(query).cursor[TVContent]
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

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVContent]] = {

    val now = currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("start" -> 1),
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContent]
    found.collect[Seq]()
  }

  override def findContentByID(contentID: String): Future[Option[TVContent]] = {
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "_id" -> BSONObjectID(contentID)))

    collection.find(query).one[TVContent]
  }

  override def findDayContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = {
    findContentByType(contentType) {
      ct =>
        val query = BSONDocument(
          "$orderby" -> BSONDocument("start" -> 1, "channel" -> 1),
          "$query" -> BSONDocument(
            "provider" -> provider,
            ct -> BSONDocument("$exists" -> true))
        )
        val found = collection.find(query).cursor[TVContent]
        found.collect[Seq]()
    }
  }


  override def findCurrentContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = {

    findContentByType(contentType) {
      ct =>
        val now = currentDate()
        val query = BSONDocument(
          "$orderby" -> BSONDocument("series.rating" -> -1, "film.rating" -> -1, "start" -> 1, "channel" -> 1),
          "$query" -> BSONDocument(
            "start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
            "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis)),
            "provider" -> provider,
            ct -> BSONDocument("$exists" -> true)))


        val found = collection.find(query).cursor[TVContent]
        found.collect[Seq]()
    }
  }

  override def findLeftContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = {

    findContentByType(contentType) {
      ct =>
        val now = currentDate()
        val query = BSONDocument(
          "$orderby" -> BSONDocument("start" -> 1, "channel" -> 1),
          "$query" -> BSONDocument(
            "provider" -> provider,
            ct -> BSONDocument("$exists" -> true),
            "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

        val found = collection.find(query).cursor[TVContent]
        found.collect[Seq]()
    }
  }

  private def findContentByType(contentType: String)(f: String => Future[Seq[TVContent]]) = {
    contentType match {
      case ct if (ct == "program" || ct == "series" || ct == "film") => f(ct)
      case _ => Future.successful(Seq())
    }
  }
}



