package models

import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import configuration.Environment._
import org.bson.types.ObjectId
import play.api.libs.json._

import com.novus.salat.Context
import mongoContext._

case class TVChannel(_id: ObjectId = new ObjectId, name: String, language: String)

object TVChannel {

  implicit object TVChannelFormat extends Format[TVChannel] {

    def writes(tvChannel: TVChannel): JsValue = {
      val tvChannelSeq = Seq(
        "name" -> JsString(tvChannel.name),
        "language" -> JsString(tvChannel.language)
      )
      JsObject(tvChannelSeq)
    }

    def reads(json: JsValue): JsResult[TVChannel] = {
      JsSuccess(TVChannel(ObjectId.get(), "", ""))
    }
  }
}

trait ChannelRepository {

  def listOfTVChannels(): Seq[TVChannel] = ???

}

class TVChannelRepository(name:String  = "tvChannel")
  extends SalatDAO[TVChannel, ObjectId](collection = MongoConnection()(mongodbDatabaseName)(name))
  with ChannelRepository   {

  override def listOfTVChannels(): Seq[TVChannel] = ???
}


class FakeChannelRepositoy extends ChannelRepository {
  override def listOfTVChannels(): Seq[TVChannel] = {
    Seq(TVChannel(ObjectId.get(), "Channel1", "ENG"),
        TVChannel(ObjectId.get(), "Channel2", "ENG"),
        TVChannel(ObjectId.get(), "Channel3", "ENG"))
  }
}


