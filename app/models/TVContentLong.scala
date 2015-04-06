package models

import org.joda.time.DateTime
import play.api.libs.iteratee.Enumerator
import _root_.utils.{TVContentSorter, TimeProvider}
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TVContent(channel: String,
                     provider: List[String],
                     start: DateTime,
                     end: DateTime,
                     rating: Option[Double],
                     series: Option[Series],
                     film: Option[Film],
                     program: Option[Program],
                     id: Option[BSONObjectID] = Some(BSONObjectID.generate))

case class Series(serieTitle: String,
                  episode: Option[Episode],
                  actors: List[String],
                  writer: List[String],
                  director: List[String],
                  genre: List[String],
                  country: List[String],
                  language: Option[String],
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

  def findCurrentContentByProvider(provider: String): Future[Seq[TVContent]] = ???

  def findLeftContentByTypeAndProvider(contentType: String, provider: String): Future[Seq[TVContent]] = ???

  def findTopLeftContentByProvider(elements: Int, provider: String): Future[Seq[TVContent]] = ???

  def findNextProgramByProvider(provider: String): Future[Seq[TVContent]] = ???

  def insertBulk(enumerator: Enumerator[TVContent]): Future[Int] = ???

  def removeAll(): Future[Boolean] = ???

}

class TVContentRepository(collectionName: String)(implicit val con: String => APIMongoConnection, val time: TimeProvider) extends ContentRepository with TVContentSorter {

  private val collection = con(collectionName).collection

  override def removeAll(): Future[Boolean] = collection.remove(BSONDocument()).map(_.ok)

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
    val now = time.currentDate()
    val query = BSONDocument(
      "$query" -> BSONDocument(
        "channel" -> channelName,
        "start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    collection.find(query).one[TVContent]
  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVContent]] = {

    val now = time.currentDate()
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
        val now = time.currentDate()
        val query = BSONDocument(
          "$orderby" -> BSONDocument("rating" -> -1, "start" -> 1, "channel" -> 1),
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
        val now = time.currentDate()
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

  override def findCurrentContentByProvider(provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("rating" -> -1, "start" -> 1, "channel" -> 1),
      "$query" -> BSONDocument("provider" -> provider,
        "start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)),
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContent]
    found.collect[Seq]()
  }

  override def findTopLeftContentByProvider(elements: Int, provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("rating" -> -1, "start" -> 1, "channel" -> 1),
      "$query" -> BSONDocument(
        "provider" -> provider,
        "end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))))

    val found = collection.find(query).cursor[TVContent]
    found.collect[Seq](elements)

  }

  override def findNextProgramByProvider(provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument(
      "$orderby" -> BSONDocument("channel" -> 1, "start" -> 1),
      "$query" -> BSONDocument(
        "provider" -> provider,
        "start" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis))
      ))

    val found = collection.find(query).cursor[TVContent]
    found.collect[Seq]().map {
      l => l.groupBy(_.channel).map(e => e._2.head).toSeq.sortWith(sortedBy)
    }
  }

  private def findContentByType(contentType: String)(f: String => Future[Seq[TVContent]]) = {
    contentType match {
      case ct if (ct == "program" || ct == "series" || ct == "film") => f(ct)
      case _ => Future.successful(Seq())
    }
  }
}



