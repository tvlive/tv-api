package acceptance.support

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import org.joda.time.DateTime

trait Mongo {

  val mongoDB: MongoDB

  def createCollection(name: String): MongoCollection = mongoDB(name)

  def dropCollection(coll: MongoCollection) = coll.drop()

  def removeCollection(coll: MongoCollection) = coll.remove(MongoDBObject())

  def insert(coll: MongoCollection, c: DBObject) = coll.insert(c)
}

object Mongo {
  def apply(db: String) = new Mongo {
    override val mongoDB: MongoDB = MongoClient()(db)
  }
}

object FilmBuilder {

  def apply(channel: String, provider: String, start: DateTime, end: DateTime,
            title: String, rating: Double, imdbId: String, posterImdb: String, id: String) =

    MongoDBObject(
      "channel" -> channel,
      "provider" -> Seq(provider),
      "start" -> start.toDate,
      "end" -> end.toDate,
      "rating" -> rating,
      "film" -> MongoDBObject(
        "title" -> title,
        "imdbId" -> imdbId,
        "posterImdb" -> posterImdb
      ),
      "_id" -> new ObjectId(id)
    )
}

object SeriesBuilder {
  def apply(channel: String, provider: String, start: DateTime, end: DateTime, title: String, epiosdeTitle: String,
            season: String, episode: String, rating: Double, imdbId: String, posterImdb: String, id: String) =

    MongoDBObject(
      "channel" -> channel,
      "provider" -> Seq(provider),
      "start" -> start.toDate,
      "end" -> end.toDate,
      "rating" -> rating,
      "series"   -> MongoDBObject(
        "serieTitle" -> title,
        "episode" -> MongoDBObject(
          "episodeTitle" -> epiosdeTitle,
          "seasonNumber" -> season,
          "episodeNumber"-> episode
        ),
        "imdbId" -> imdbId,
        "posterImdb" -> posterImdb
      ),
      "_id" -> new ObjectId(id)
    )
}
object ProgramBuilder {
  //
    def apply(channel: String, provider: String, start: DateTime, end: DateTime, title: String, id: String) =
    MongoDBObject(
      "channel" -> channel,
      "provider" -> Seq(provider),
      "start" -> start.toDate,
      "end" -> end.toDate,
      "program"   -> MongoDBObject("title" -> title),
      "_id" -> new ObjectId(id)
    )
  }


