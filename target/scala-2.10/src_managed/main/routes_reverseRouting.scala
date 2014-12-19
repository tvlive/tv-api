// @SOURCE:/Users/alvaro/personal/tv-api/conf/routes
// @HASH:3bba2f60a224f95950c04f772956c2c7141e5a53
// @DATE:Sun Oct 19 21:44:06 BST 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:34
// @LINE:30
// @LINE:28
// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers {

// @LINE:6
class ReverseTVRootController {
    

// @LINE:6
def roots(): Call = {
   Call("GET", _prefix)
}
                                                
    
}
                          

// @LINE:34
class ReverseAssets {
    

// @LINE:34
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:30
class ReverseTVChannelProviderController {
    

// @LINE:30
def providers(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "providers/channels")
}
                                                
    
}
                          

// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
class ReverseTVContentController {
    

// @LINE:20
def tvContentDetails(id:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                                                

// @LINE:22
def contentByType(playframework_escape_type:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/type/" + implicitly[PathBindable[String]].unbind("type", dynamicString(playframework_escape_type)) + "/today")
}
                                                

// @LINE:16
def allContent(channelName:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/channel/" + implicitly[PathBindable[String]].unbind("channelName", dynamicString(channelName)) + "/today")
}
                                                

// @LINE:14
def currentContent(channelName:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/channel/" + implicitly[PathBindable[String]].unbind("channelName", dynamicString(channelName)) + "/current")
}
                                                

// @LINE:24
def currentContentByType(playframework_escape_type:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/type/" + implicitly[PathBindable[String]].unbind("type", dynamicString(playframework_escape_type)) + "/current")
}
                                                

// @LINE:26
def contentLeftByType(playframework_escape_type:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/type/" + implicitly[PathBindable[String]].unbind("type", dynamicString(playframework_escape_type)) + "/left")
}
                                                

// @LINE:18
def contentLeft(channelName:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "tvcontent/channel/" + implicitly[PathBindable[String]].unbind("channelName", dynamicString(channelName)) + "/left")
}
                                                
    
}
                          

// @LINE:12
// @LINE:10
// @LINE:8
class ReverseTVChannelController {
    

// @LINE:10
def channelsByCategory(category:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "channels/category/" + implicitly[PathBindable[String]].unbind("category", dynamicString(category)))
}
                                                

// @LINE:8
def channels(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "channels")
}
                                                

// @LINE:12
def channelsByProvider(provider:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "channels/provider/" + implicitly[PathBindable[String]].unbind("provider", dynamicString(provider)))
}
                                                
    
}
                          

// @LINE:28
class ReverseTVChannelCategoryController {
    

// @LINE:28
def categories(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "categories/channels")
}
                                                
    
}
                          
}
                  


// @LINE:34
// @LINE:30
// @LINE:28
// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers.javascript {

// @LINE:6
class ReverseTVRootController {
    

// @LINE:6
def roots : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVRootController.roots",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        
    
}
              

// @LINE:34
class ReverseAssets {
    

// @LINE:34
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:30
class ReverseTVChannelProviderController {
    

// @LINE:30
def providers : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVChannelProviderController.providers",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "providers/channels"})
      }
   """
)
                        
    
}
              

// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
class ReverseTVContentController {
    

// @LINE:20
def tvContentDetails : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.tvContentDetails",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:22
def contentByType : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.contentByType",
   """
      function(type) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/type/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("type", encodeURIComponent(type)) + "/today"})
      }
   """
)
                        

// @LINE:16
def allContent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.allContent",
   """
      function(channelName) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/channel/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("channelName", encodeURIComponent(channelName)) + "/today"})
      }
   """
)
                        

// @LINE:14
def currentContent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.currentContent",
   """
      function(channelName) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/channel/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("channelName", encodeURIComponent(channelName)) + "/current"})
      }
   """
)
                        

// @LINE:24
def currentContentByType : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.currentContentByType",
   """
      function(type) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/type/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("type", encodeURIComponent(type)) + "/current"})
      }
   """
)
                        

// @LINE:26
def contentLeftByType : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.contentLeftByType",
   """
      function(type) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/type/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("type", encodeURIComponent(type)) + "/left"})
      }
   """
)
                        

// @LINE:18
def contentLeft : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVContentController.contentLeft",
   """
      function(channelName) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tvcontent/channel/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("channelName", encodeURIComponent(channelName)) + "/left"})
      }
   """
)
                        
    
}
              

// @LINE:12
// @LINE:10
// @LINE:8
class ReverseTVChannelController {
    

// @LINE:10
def channelsByCategory : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVChannelController.channelsByCategory",
   """
      function(category) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "channels/category/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("category", encodeURIComponent(category))})
      }
   """
)
                        

// @LINE:8
def channels : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVChannelController.channels",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "channels"})
      }
   """
)
                        

// @LINE:12
def channelsByProvider : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVChannelController.channelsByProvider",
   """
      function(provider) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "channels/provider/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("provider", encodeURIComponent(provider))})
      }
   """
)
                        
    
}
              

// @LINE:28
class ReverseTVChannelCategoryController {
    

// @LINE:28
def categories : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.TVChannelCategoryController.categories",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "categories/channels"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:34
// @LINE:30
// @LINE:28
// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers.ref {


// @LINE:6
class ReverseTVRootController {
    

// @LINE:6
def roots(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVRootController.roots(), HandlerDef(this, "controllers.TVRootController", "roots", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      
    
}
                          

// @LINE:34
class ReverseAssets {
    

// @LINE:34
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:30
class ReverseTVChannelProviderController {
    

// @LINE:30
def providers(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVChannelProviderController.providers(), HandlerDef(this, "controllers.TVChannelProviderController", "providers", Seq(), "GET", """""", _prefix + """providers/channels""")
)
                      
    
}
                          

// @LINE:26
// @LINE:24
// @LINE:22
// @LINE:20
// @LINE:18
// @LINE:16
// @LINE:14
class ReverseTVContentController {
    

// @LINE:20
def tvContentDetails(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.tvContentDetails(id), HandlerDef(this, "controllers.TVContentController", "tvContentDetails", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/$id<[^/]+>""")
)
                      

// @LINE:22
def contentByType(playframework_escape_type:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.contentByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "contentByType", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/type/$type<[^/]+>/today""")
)
                      

// @LINE:16
def allContent(channelName:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.allContent(channelName), HandlerDef(this, "controllers.TVContentController", "allContent", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/channel/$channelName<[^/]+>/today""")
)
                      

// @LINE:14
def currentContent(channelName:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.currentContent(channelName), HandlerDef(this, "controllers.TVContentController", "currentContent", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/channel/$channelName<[^/]+>/current""")
)
                      

// @LINE:24
def currentContentByType(playframework_escape_type:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.currentContentByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "currentContentByType", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/type/$type<[^/]+>/current""")
)
                      

// @LINE:26
def contentLeftByType(playframework_escape_type:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.contentLeftByType(playframework_escape_type), HandlerDef(this, "controllers.TVContentController", "contentLeftByType", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/type/$type<[^/]+>/left""")
)
                      

// @LINE:18
def contentLeft(channelName:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVContentController.contentLeft(channelName), HandlerDef(this, "controllers.TVContentController", "contentLeft", Seq(classOf[String]), "GET", """""", _prefix + """tvcontent/channel/$channelName<[^/]+>/left""")
)
                      
    
}
                          

// @LINE:12
// @LINE:10
// @LINE:8
class ReverseTVChannelController {
    

// @LINE:10
def channelsByCategory(category:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVChannelController.channelsByCategory(category), HandlerDef(this, "controllers.TVChannelController", "channelsByCategory", Seq(classOf[String]), "GET", """""", _prefix + """channels/category/$category<[^/]+>""")
)
                      

// @LINE:8
def channels(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVChannelController.channels(), HandlerDef(this, "controllers.TVChannelController", "channels", Seq(), "GET", """""", _prefix + """channels""")
)
                      

// @LINE:12
def channelsByProvider(provider:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVChannelController.channelsByProvider(provider), HandlerDef(this, "controllers.TVChannelController", "channelsByProvider", Seq(classOf[String]), "GET", """""", _prefix + """channels/provider/$provider<[^/]+>""")
)
                      
    
}
                          

// @LINE:28
class ReverseTVChannelCategoryController {
    

// @LINE:28
def categories(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.TVChannelCategoryController.categories(), HandlerDef(this, "controllers.TVChannelCategoryController", "categories", Seq(), "GET", """""", _prefix + """categories/channels""")
)
                      
    
}
                          
}
        
    