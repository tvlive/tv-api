package models

import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.iteratee.Enumerator
import _root_.utils.{TVContentSorter, TimeProvider}
import _root_.utils.mongoBuilder._
import reactivemongo.api.indexes.{IndexType, Index}
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
                  posterImdb: Option[String],
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
                posterImdb: Option[String],
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

  def searchBy(provider: String, title: Option[String], content: Option[String], rating: Option[Double]): Future[Seq[TVContent]] = ???

  def removeAll(): Future[Boolean] = ???

  def drop(): Future[Boolean] = ???

}

class TVContentRepository(collectionName: String)(implicit val con: String => APIMongoConnection, val time: TimeProvider) extends ContentRepository with TVContentSorter {

  private val collection = con(collectionName).collection

  createIndex

  override def drop(): Future[Boolean] = collection.drop()

  override def removeAll(): Future[Boolean] = collection.remove(BSONDocument()).map(_.ok)

  override def insertBulk(channels: Enumerator[TVContent]): Future[Int] = collection.bulkInsert(channels)

  override def findDayContentByChannel(channelName: String): Future[Seq[TVContent]] = {
    val query = BSONDocument().channel(channelName)
    val orderby = BSONDocument().startAsc()

    findContent(query, orderby)
  }

  override def findCurrentContentByChannel(channelName: String): Future[Option[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().channel(channelName).startLte(now).endGt(now)

    collection.find(query).one[TVContent]
  }

  override def findLeftContentByChannel(channelName: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().channel(channelName).endGt(now)
    val orderBy = BSONDocument().startAsc()

    findContent(query, orderBy)
  }

  override def findContentByID(contentID: String): Future[Option[TVContent]] = {
    collection.find(BSONDocument().id(contentID)).one[TVContent]
  }

  override def findDayContentByTypeAndProvider(tvcontentType: String, provider: String): Future[Seq[TVContent]] = {
    val query = BSONDocument().provider(provider).exist(tvcontentType)
    val orderBy = BSONDocument().startAsc().channelAsc()

    findContent(query, orderBy)
  }

  override def findCurrentContentByTypeAndProvider(tvcontentType: String, provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().startLte(now).endGt(now).provider(provider).exist(tvcontentType)
    val orderBy = BSONDocument().ratingDesc().startAsc().channelAsc()

    findContent(query, orderBy)
  }

  override def findLeftContentByTypeAndProvider(tvcontentType: String, provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().provider(provider).exist(tvcontentType).endGt(now)
    val orderBy = BSONDocument().startAsc().channelAsc()

    findContent(query, orderBy)
  }

  override def findCurrentContentByProvider(provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().provider(provider).startLte(now).endGt(now)
    val orderBy = BSONDocument().ratingDesc().startAsc().channelAsc()

    findContent(query, orderBy)
  }

  override def findTopLeftContentByProvider(elements: Int, provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().provider(provider).endGt(now)
    val orderBy = BSONDocument().ratingDesc().startAsc().channelAsc()

    findContent(query, orderBy, elements)
  }

  override def findNextProgramByProvider(provider: String): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().provider(provider).startGt(now)
    val orderBy = BSONDocument().channelAsc().startAsc()

    findContent(query, orderBy).map {
      l => l.groupBy(_.channel).map {
        case (_, e) => e.head
      }.toSeq.sortWith(sortedBy)
    }
  }

  override def searchBy(provider: String, title: Option[String], content: Option[String], rating: Option[Double]): Future[Seq[TVContent]] = {
    val now = time.currentDate()
    val query = BSONDocument().provider(provider) ++
      title.map("\"" + _ + "\"").map(BSONDocument().title(_)).getOrElse(BSONDocument()) ++
      content.map(BSONDocument().exist(_)).getOrElse(BSONDocument()) ++
      rating.map(BSONDocument().rating(_)).getOrElse(BSONDocument())

    val orderBy = BSONDocument().ratingDesc().startAsc().channelAsc()

    findContent(query, orderBy)
  }

  private def findContent(q: BSONDocument, o: BSONDocument, e: Int = Int.MaxValue): Future[Seq[TVContent]] = {
    collection.find(q).sort(o).cursor[TVContent].collect[Seq](e)
  }


  private def createIndex(): Future[Boolean] = {
    collection.indexesManager.ensure(
      Index(
        key = Seq(
          "series.episode.episodeTitle" -> IndexType.Text,
          "series.serieTitle" -> IndexType.Text,
          "film.title" -> IndexType.Text,
          "program.title" -> IndexType.Text),
        name = Some("search_name"),
        unique = false,
        options = BSONDocument("default_language" -> "en", "language_override" -> "en"))
    ).map {
      ok =>
        Logger.info(s"Index created for collection ${collectionName}: $ok")
        ok
    }.recover {
      case e =>
        Logger.error("Error creating index", e)
        false
    }
  }
}
