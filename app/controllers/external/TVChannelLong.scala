package controllers.external

import java.net.URLEncoder

import models.TVChannel
import utils.URLBuilder

case class TVChannelLong(name: String,
                         provider: List[String],
                         category: List[String],
                         uriToday: String,
                         uriCurrent: String,
                         uriLeft: String,
                         image: String)


object ChannelLong extends URLBuilder {
  def apply(tvc: TVChannel)(implicit host: String): TVChannelLong = {
    val uriToday: String = buildUrl(host, controllers.routes.TVContentController.allContent(URLEncoder.encode(tvc.name, "UTF-8")).url)
    val uriCurrent: String = buildUrl(host, controllers.routes.TVContentController.currentContent(URLEncoder.encode(tvc.name, "UTF-8")).url)
    val uriLeft: String = buildUrl(host, controllers.routes.TVContentController.contentLeft(URLEncoder.encode(tvc.name,"UTF-8")).url)
      val image = buildImageUrl(host, "/", buildImageName(tvc.name))

    TVChannelLong(tvc.name, tvc.provider, tvc.category, uriToday, uriCurrent, uriLeft, image)
  }
}