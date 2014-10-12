import models._
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

  implicit val reviewReads: Reads[TVChannel] = (
    (__ \ "name").read[String] and
      (__ \ "provider").read[List[String]] and
      (__ \ "category").read[List[String]] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVChannel.apply _)

  implicit val tvChannelWrites = new Writes[TVChannel] {
    override def writes(tvchannel: TVChannel): JsValue = Json.obj(
      "name" -> tvchannel.name,
      "provider" -> tvchannel.provider,
      "category" -> tvchannel.category,
      "id" -> tvchannel.id,
      "uriToday" -> tvchannel.uriToday,
      "uriCurrent" -> tvchannel.uriCurrent,
      "uriLeft" -> tvchannel.uriLeft
    )
  }

  implicit val filmFmt = Json.format[Film]
  implicit val serieFmt = Json.format[Series]
  implicit val programFmt = Json.format[Program]

  val pattern = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val filmShortFmt = Json.format[FilmShort]
  implicit val serieShortFmt = Json.format[SeriesShort]
  implicit val programShortFmt = Json.format[ProgramShort]


  implicit val tvProgramReads: Reads[TVContent] = ((__ \ "channel").read[String] and
      (__ \ "start").read[DateTime].map[DateTime]{ dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))} and
      (__ \ "end").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "category").read[Option[List[String]]] and
      (__ \ "series").read[Option[Series]] and
      (__ \ "film").read[Option[Film]] and
      (__ \ "program").read[Option[Program]] and
      (__ \ "id").read[Option[BSONObjectID]]
      )(TVContent.apply _)

  implicit val tvProgramWrites = new Writes[TVContent] {
    override def writes(tvContent: TVContent): JsValue = Json.obj(
      "channel" -> tvContent.channel,
      "start" -> tvContent.start.toDateTime(DateTimeZone.forID("Europe/London")),
      "end" -> tvContent.end.toDateTime(DateTimeZone.forID("Europe/London")),
      "category" -> tvContent.category,
      "series" -> tvContent.series,
      "film" -> tvContent.film,
      "program" -> tvContent.program,
      "id" -> tvContent.id
    )
  }

  implicit val tvProgramShortReads: Reads[TVContentShort] = (
    (__ \ "channel").read[String] and
      (__ \ "start").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "end").read[DateTime].map[DateTime](dt => dt.withZoneRetainFields(DateTimeZone.forID("Europe/London"))) and
      (__ \ "category").read[Option[List[String]]] and
      (__ \ "series").read[Option[SeriesShort]] and
      (__ \ "film").read[Option[FilmShort]] and
      (__ \ "program").read[Option[ProgramShort]] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVContentShort.apply _)


  implicit val tvProgramShortWrites = new Writes[TVContentShort] {
    override def writes(tvContent: TVContentShort): JsValue = Json.obj(
      "channel" -> tvContent.channel,
      "start" -> tvContent.start.toDateTime(DateTimeZone.forID("Europe/London")),
      "end" -> tvContent.end.toDateTime(DateTimeZone.forID("Europe/London")),
      "category" -> tvContent.category,
      "series" -> tvContent.series,
      "film" -> tvContent.film,
      "program" -> tvContent.program,
      "uriTVProgramDetails" -> tvContent.uriTVProgramDetails,
      "id" -> tvContent.id
    )
  }


  implicit val tvChannelCategoryFmt = Json.format[TVChannelCategory]
  implicit val tvChannelProviderFmt = Json.format[TVChannelProvider]
  implicit val tvContentGenreFmt = Json.format[TVContentGenre]
}
