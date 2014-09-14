import _root_.utils.TimeProvider
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson._
import scala.util.Try
import models._

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
      (__ \ "genre").read[String] and
      (__ \ "language").read[String] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVChannel.apply _)

  implicit val tvChannelWrites = new Writes[TVChannel] {
    override def writes(tvchannel: TVChannel): JsValue = Json.obj(
      "name" -> tvchannel.name,
      "genre" -> tvchannel.genre,
      "language" -> tvchannel.language,
      "id" -> tvchannel.id,
      "uriToday" -> tvchannel.uriToday,
      "uriCurrent" -> tvchannel.uriCurrent,
      "uriLeft" -> tvchannel.uriLeft
    )
  }

  implicit val programFmt = Json.format[Program]
  implicit val serieFmt = Json.format[Serie]
  val pattern = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val tvProgramFmt = Json.format[TVProgram]
  implicit val programShortFmt = Json.format[ProgramShort]
  implicit val serieShortFmt = Json.format[SerieShort]

  implicit val tvProgramShortReads: Reads[TVProgramShort] = (
    (__ \ "channel").read[String] and
      (__ \ "start").read[DateTime] and
      (__ \ "end").read[DateTime] and
      (__ \ "category").read[Option[List[String]]] and
      (__ \ "series").read[Option[SerieShort]] and
      (__ \ "program").read[Option[ProgramShort]] and
      (__ \ "id").read[Option[BSONObjectID]]
    )(TVProgramShort.apply _)


  implicit val tvProgramShortWrites = new Writes[TVProgramShort] {
    override def writes(tvprogram: TVProgramShort): JsValue = Json.obj(
      "channel" -> tvprogram.channel,
      "start" -> tvprogram.startTime,
      "end" -> tvprogram.endTime,
      "category" -> tvprogram.category,
      "series" -> tvprogram.series,
      "program" -> tvprogram.program,
      "uriTVProgramDetails" -> tvprogram.uriTVProgramDetails,
      "id" -> tvprogram.id
    )
  }
}
