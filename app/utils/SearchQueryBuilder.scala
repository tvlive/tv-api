package utils

import org.joda.time.DateTime
import reactivemongo.bson.{BSONObjectID, BSONDocument, BSONDateTime}


object mongoBuilder {
  implicit class SearchQueryBuilder(doc: BSONDocument) {
      def title(t: String) = doc ++ BSONDocument("$text" -> BSONDocument("$search" -> t))
      def rating(r: Double) = doc ++ BSONDocument("rating" -> r)
      def id(i: String) = doc ++ BSONDocument("_id" -> BSONObjectID(i))
      def provider(p: String) = doc ++ BSONDocument("provider" -> p)
      def channel(c: String) = doc ++ BSONDocument("channel" -> c)
      def category(c: String) = doc ++ BSONDocument("category" -> c)
      def startLte(now: DateTime) = doc ++ BSONDocument("start" -> BSONDocument("$lte" -> BSONDateTime(now.getMillis)))
      def startGt(now: DateTime) = doc ++ BSONDocument("start" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis)))
      def endGt(now: DateTime) = doc ++ BSONDocument("end" -> BSONDocument("$gt" -> BSONDateTime(now.getMillis)))
      def exist(key: String) = doc ++ BSONDocument(key -> BSONDocument("$exists" -> true))
  }

  //  BSONDocument("rating" -> -1, "start" -> 1, "channel" -> 1)
  implicit class OrderQueryBuilder(doc: BSONDocument) {
    def ratingDesc() = doc ++ BSONDocument("rating" -> -1)
    def startAsc() = doc ++ BSONDocument("start" -> 1)
    def channelAsc() = doc ++ BSONDocument("channel" -> 1)
    def providerAsc() = doc ++ BSONDocument("provider" -> 1)
    def categoryAsc() = doc ++ BSONDocument("category" -> 1)
    def nameAsc() = doc ++ BSONDocument("name" -> 1)

  }
}

