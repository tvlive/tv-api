import controllers.external._
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._

import scala.util.Try

package object controllers {

  implicit object BSONObjectIDFormat extends Format[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = JsString(objectId.stringify)

    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(x) => {
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(x)
        if (maybeOID.isSuccess) JsSuccess(maybeOID.get)
        else {
          JsError("Expected BSONObjectID as JsString")
        }
      }
      case _ => JsError("Expected BSONObjectID as JsString")
    }
  }

  implicit val reviewReads: Reads[TVChannelLong] = (
    (__ \ "name").read[String] and
      (__ \ "provider").read[List[String]] and
      (__ \ "category").read[List[String]] and
      (__ \ "uriToday").read[String] and
      (__ \ "uriCurrent").read[String] and
      (__ \ "uriLeft").read[String] and
      (__ \ "image").read[String]
    )(TVChannelLong.apply _)

  implicit val tvChannelWrites = new Writes[TVChannelLong] {
    override def writes(tvchannel: TVChannelLong): JsValue = Json.obj(
      "name" -> tvchannel.name,
      "provider" -> tvchannel.provider,
      "category" -> tvchannel.category,
      "uriToday" -> tvchannel.uriToday,
      "uriCurrent" -> tvchannel.uriCurrent,
      "uriLeft" -> tvchannel.uriLeft,
      "image" -> tvchannel.image
    )
  }

  implicit val filmFmt = Json.format[FilmLong]
  implicit val episodeFmt = Json.format[EpisodeLong]
  implicit val serieFmt = Json.format[SeriesLong]
  implicit val programFmt = Json.format[ProgramLong]

  val pattern = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val filmShortFmt = Json.format[FilmShort]
  implicit val episodeShortFmt = Json.format[EpisodeShort]
  implicit val serieShortFmt = Json.format[SeriesShort]
  implicit val programShortFmt = Json.format[ProgramShort]


  implicit val tvProgramReads: Reads[TVContentLong] = (
    (__ \ "channel").read[String] and
      (__ \ "channelImageURL").read[String] and
      (__ \ "provider").read[List[String]] and
      (__ \ "start").read[DateTime].map[DateTime] { dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))} and
      (__ \ "end").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "rating").read[Option[Double]] and
      (__ \ "series").read[Option[SeriesLong]] and
      (__ \ "film").read[Option[FilmLong]] and
      (__ \ "program").read[Option[ProgramLong]] and
      (__ \ "onTimeNow").read[Boolean] and
      (__ \ "minutesLeft").read[Option[Long]]
    )(TVContentLong.apply _)

  implicit val tvProgramWrites = new Writes[TVContentLong] {
    override def writes(tvContentLong: TVContentLong): JsValue = Json.obj(
      "channel" -> tvContentLong.channel,
      "channelImageURL" -> tvContentLong.channelImageURL,
      "provider" -> tvContentLong.provider,
      "start" -> tvContentLong.start.toDateTime(DateTimeZone.forID("Europe/London")),
      "end" -> tvContentLong.end.toDateTime(DateTimeZone.forID("Europe/London")),
      "rating" -> tvContentLong.rating,
      "series" -> tvContentLong.series,
      "film" -> tvContentLong.film,
      "program" -> tvContentLong.program,
      "onTimeNow" -> tvContentLong.onTimeNow,
      "minutesLeft" -> tvContentLong.minutesLeft
    )
  }

  implicit val tvProgramShortReads: Reads[TVContentShort] = (
    (__ \ "channel").read[String] and
      (__ \ "channelImageURL").read[String] and
      (__ \ "provider").read[List[String]] and
      (__ \ "start").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "end").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "rating").read[Option[Double]] and
      (__ \ "series").read[Option[SeriesShort]] and
      (__ \ "film").read[Option[FilmShort]] and
      (__ \ "program").read[Option[ProgramShort]] and
      (__ \ "onTimeNow").read[Boolean] and
      (__ \ "minutesLeft").read[Option[Long]] and
      (__ \ "uriTVContentDetails").read[String]
    )(TVContentShort.apply _)


  implicit val tvProgramShortWrites = new Writes[TVContentShort] {
    override def writes(tvContentShort: TVContentShort): JsValue = Json.obj(
      "channel" -> tvContentShort.channel,
      "channelImageURL" -> tvContentShort.channelImageURL,
      "provider" -> tvContentShort.provider,
      "start" -> tvContentShort.start.toDateTime(DateTimeZone.forID("Europe/London")),
      "end" -> tvContentShort.end.toDateTime(DateTimeZone.forID("Europe/London")),
      "rating" -> tvContentShort.rating,
      "series" -> tvContentShort.series,
      "film" -> tvContentShort.film,
      "program" -> tvContentShort.program,
      "uriTVContentDetails" -> tvContentShort.uriTVContentDetails,
      "onTimeNow" -> tvContentShort.onTimeNow,
      "minutesLeft" -> tvContentShort.minutesLeft
    )
  }


  implicit val tvChannelCategoryExtFmt = Json.format[TVChannelCategoryExternal]
  implicit val tvChannelProviderExtFmt = Json.format[TVChannelProviderExternal]
  implicit val linkFmt = Json.format[Link]
  implicit val notFoundResponseFmt = Json.format[NotFoundResponse]
  implicit val internalServerErrorResponseFmt = Json.format[InternalErrorServerResponse]
  implicit val BadRequestResponseFmt = Json.format[BadRequestResponse]

}
