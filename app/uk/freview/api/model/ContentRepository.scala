package uk.freview.api.model

import play.api.libs.json._


case class TVChannel(name: String, language: String)

object TVChannel {

  implicit object TVChannelFormat extends Format[TVChannel] {

    def writes(tvChannel: TVChannel): JsValue = {
      val tvChannelSeq = Seq(
        "name" -> JsString(tvChannel.name),
        "tweet" -> JsString(tvChannel.language)
      )
      JsObject(tvChannelSeq)
    }

    def reads(json: JsValue): JsResult[TVChannel] = {
      JsSuccess(TVChannel("", ""))
    }

  }

}


trait TVRepository {

  def listOfTVChannels(): Seq[TVChannel]

}

class FakeRepositoy extends TVRepository {
  override def listOfTVChannels(): Seq[TVChannel] = {
    Seq(TVChannel("Channel1","ENG"), TVChannel("Channel2","ENG"), TVChannel("Channel3","ENG"))
  }
}
