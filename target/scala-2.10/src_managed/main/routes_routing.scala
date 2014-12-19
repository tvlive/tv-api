// @SOURCE:/Users/alvaro/personal/tv-api/conf/routes
// @HASH:3bba2f60a224f95950c04f772956c2c7141e5a53
// @DATE:Sun Oct 19 21:44:06 BST 2014


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_TVRootController_roots0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:8
private[this] lazy val controllers_TVChannelController_channels1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("channels"))))
        

// @LINE:10
private[this] lazy val controllers_TVChannelController_channelsByCategory2 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("channels/category/"),DynamicPart("category", """[^/]+""",true))))
        

// @LINE:12
private[this] lazy val controllers_TVChannelController_channelsByProvider3 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("channels/provider/"),DynamicPart("provider", """[^/]+""",true))))
        

// @LINE:14
private[this] lazy val controllers_TVContentController_currentContent4 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/channel/"),DynamicPart("channelName", """[^/]+""",true),StaticPart("/current"))))
        

// @LINE:16
private[this] lazy val controllers_TVContentController_allContent5 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/channel/"),DynamicPart("channelName", """[^/]+""",true),StaticPart("/today"))))
        

// @LINE:18
private[this] lazy val controllers_TVContentController_contentLeft6 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/channel/"),DynamicPart("channelName", """[^/]+""",true),StaticPart("/left"))))
        

// @LINE:20
private[this] lazy val controllers_TVContentController_tvContentDetails7 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/"),DynamicPart("id", """[^/]+""",true))))
        

// @LINE:22
private[this] lazy val controllers_TVContentController_contentByType8 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/type/"),DynamicPart("type", """[^/]+""",true),StaticPart("/today"))))
        

// @LINE:24
private[this] lazy val controllers_TVContentController_currentContentByType9 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/type/"),DynamicPart("type", """[^/]+""",true),StaticPart("/current"))))
        

// @LINE:26
private[this] lazy val controllers_TVContentController_contentLeftByType10 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("tvcontent/type/"),DynamicPart("type", """[^/]+""",true),StaticPart("/left"))))
        

// @LINE:28
private[this] lazy val controllers_TVChannelCategoryController_categories11 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("categories/channels"))))
        

// @LINE:30
private[this] lazy val controllers_TVChannelProviderController_providers12 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("providers/channels"))))
        

// @LINE:34
private[this] lazy val controllers_Assets_at13 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        
def documentation = List(("""GET""", prefix,"""controllers.TVRootController.roots"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """channels""","""controllers.TVChannelController.channels"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """channels/category/$category<[^/]+>""","""controllers.TVChannelController.channelsByCategory(category:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """channels/provider/$provider<[^/]+>""","""controllers.TVChannelController.channelsByProvider(provider:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/channel/$channelName<[^/]+>/current""","""controllers.TVContentController.currentContent(channelName:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/channel/$channelName<[^/]+>/today""","""controllers.TVContentController.allContent(channelName:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/channel/$channelName<[^/]+>/left""","""controllers.TVContentController.contentLeft(channelName:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/$id<[^/]+>""","""controllers.TVContentController.tvContentDetails(id:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/type/$type<[^/]+>/today""","""controllers.TVContentController.contentByType(type:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/type/$type<[^/]+>/current""","""controllers.TVContentController.currentContentByType(type:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """tvcontent/type/$type<[^/]+>/left""","""controllers.TVContentController.contentLeftByType(type:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """categories/channels""","""controllers.TVChannelCategoryController.categories"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """providers/channels""","""controllers.TVChannelProviderController.providers"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_TVRootController_roots0(params) => {
   call { 
        invokeHandler(controllers.TVRootController.roots, HandlerDef(this, "controllers.TVRootController", "roots", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:8
case controllers_TVChannelController_channels1(params) => {
   call { 
        invokeHandler(controllers.TVChannelController.channels, HandlerDef(this, "controllers.TVChannelController", "channels", Nil,"GET", """""", Routes.prefix + """channels"""))
   }
}
        

// @LINE:10
case controllers_TVChannelController_channelsByCategory2(params) => {
   call(params.fromPath[String]("category", None)) { (category) =>
        invokeHandler(controllers.TVChannelController.channelsByCategory(category), HandlerDef(this, "controllers.TVChannelController", "channelsByCategory", Seq(classOf[String]),"GET", """""", Routes.prefix + """channels/category/$category<[^/]+>"""))
   }
}
        

// @LINE:12
case controllers_TVChannelController_channelsByProvider3(params) => {
   call(params.fromPath[String]("provider", None)) { (provider) =>
        invokeHandler(controllers.TVChannelController.channelsByProvider(provider), HandlerDef(this, "controllers.TVChannelController", "channelsByProvider", Seq(classOf[String]),"GET", """""", Routes.prefix + """channels/provider/$provider<[^/]+>"""))
   }
}
        

// @LINE:14
case controllers_TVContentController_currentContent4(params) => {
   call(params.fromPath[String]("channelName", None)) { (channelName) =>
        invokeHandler(controllers.TVContentController.currentContent(channelName), HandlerDef(this, "controllers.TVContentController", "currentContent", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/channel/$channelName<[^/]+>/current"""))
   }
}
        

// @LINE:16
case controllers_TVContentController_allContent5(params) => {
   call(params.fromPath[String]("channelName", None)) { (channelName) =>
        invokeHandler(controllers.TVContentController.allContent(channelName), HandlerDef(this, "controllers.TVContentController", "allContent", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/channel/$channelName<[^/]+>/today"""))
   }
}
        

// @LINE:18
case controllers_TVContentController_contentLeft6(params) => {
   call(params.fromPath[String]("channelName", None)) { (channelName) =>
        invokeHandler(controllers.TVContentController.contentLeft(channelName), HandlerDef(this, "controllers.TVContentController", "contentLeft", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/channel/$channelName<[^/]+>/left"""))
   }
}
        

// @LINE:20
case controllers_TVContentController_tvContentDetails7(params) => {
   call(params.fromPath[String]("id", None)) { (id) =>
        invokeHandler(controllers.TVContentController.tvContentDetails(id), HandlerDef(this, "controllers.TVContentController", "tvContentDetails", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/$id<[^/]+>"""))
   }
}
        

// @LINE:22
case controllers_TVContentController_contentByType8(params) => {
   call(params.fromPath[String]("type", None)) { (playframework_escape_type) =>
        invokeHandler(controllers.TVContentController.contentByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "contentByType", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/type/$type<[^/]+>/today"""))
   }
}
        

// @LINE:24
case controllers_TVContentController_currentContentByType9(params) => {
   call(params.fromPath[String]("type", None)) { (playframework_escape_type) =>
        invokeHandler(controllers.TVContentController.currentContentByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "currentContentByType", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/type/$type<[^/]+>/current"""))
   }
}
        

// @LINE:26
case controllers_TVContentController_contentLeftByType10(params) => {
   call(params.fromPath[String]("type", None)) { (playframework_escape_type) =>
        invokeHandler(controllers.TVContentController.contentLeftByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "contentLeftByType", Seq(classOf[String]),"GET", """""", Routes.prefix + """tvcontent/type/$type<[^/]+>/left"""))
   }
}
        

// @LINE:28
case controllers_TVChannelCategoryController_categories11(params) => {
   call { 
        invokeHandler(controllers.TVChannelCategoryController.categories, HandlerDef(this, "controllers.TVChannelCategoryController", "categories", Nil,"GET", """""", Routes.prefix + """categories/channels"""))
   }
}
        

// @LINE:30
case controllers_TVChannelProviderController_providers12(params) => {
   call { 
        invokeHandler(controllers.TVChannelProviderController.providers, HandlerDef(this, "controllers.TVChannelProviderController", "providers", Nil,"GET", """""", Routes.prefix + """providers/channels"""))
   }
}
        

// @LINE:34
case controllers_Assets_at13(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        
}

}
     