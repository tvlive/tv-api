
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(Seq[Any](/*3.2*/main("Welcome to Play")/*3.25*/ {_display_(Seq[Any](format.raw/*3.27*/("""

    """),_display_(Seq[Any](/*5.6*/play20/*5.12*/.welcome(message))),format.raw/*5.29*/("""

""")))})),format.raw/*7.2*/("""
"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Sun Oct 19 21:44:07 BST 2014
                    SOURCE: /Users/alvaro/personal/tv-api/app/views/index.scala.html
                    HASH: 080fc198deedd12cb8158e301d69b93c2ab76a30
                    MATRIX: 556->1|667->18|704->21|735->44|774->46|815->53|829->59|867->76|900->79
                    LINES: 19->1|22->1|24->3|24->3|24->3|26->5|26->5|26->5|28->7
                    -- GENERATED --
                */
            