package controllers.external

import java.net.URLEncoder

import models.TVChannel
import reactivemongo.bson.BSONObjectID
import utils.URLBuilder

case class TVChannelLong(name: String,
                         provider: List[String],
                         category: List[String],
                         uriToday: String,
                         uriCurrent: String,
                         uriLeft: String,
                         image: String,
                         id: Option[BSONObjectID] = Some(BSONObjectID.generate))


object ChannelLong extends URLBuilder {
  def apply(tvc: TVChannel): TVChannelLong = {
      val uriToday: String = controllers.routes.TVContentController.allContent(URLEncoder.encode(tvc.name, "UTF-8")).url
      val uriCurrent: String = controllers.routes.TVContentController.currentContent(URLEncoder.encode(tvc.name, "UTF-8")).url
      val uriLeft: String = controllers.routes.TVContentController.contentLeft(URLEncoder.encode(tvc.name,"UTF-8")).url
      val image = buildImageUrl(tvc.name)

    TVChannelLong(tvc.name, tvc.provider, tvc.category, uriToday, uriCurrent, uriLeft, image, tvc.id)
  }
}