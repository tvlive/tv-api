package controllers

import configuration.ApplicationContext._
import configuration.Environment
import models.{ChannelCategoryRepository, ChannelProviderRepository}
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.Action
import utils.URLBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TVRootController extends TVRootController {
  override val providerRepository = tvChannelProviderReporitory
  override val categoryRepository = tvChannelCategoryRepository
  override implicit val host: String = Environment.host
}

trait TVRootController extends BaseController with URLBuilder {
  implicit val host: String
  val providerRepository: ChannelProviderRepository
  val categoryRepository: ChannelCategoryRepository

  def roots = Action.async {

    val r = for {
      providers <- providerRepository.findAll()
      categories <- categoryRepository.findAll()
    } yield (providers, categories)

    val links: Future[Seq[Link]] = r.map {
      case (providers, categories) =>
        val linkProviders: Seq[Link] = providers.map {
          p => Link(
            buildUrl(host, controllers.routes.TVChannelController.channelsByProvider(p.provider).url),
            Messages("provider.uri", p.provider))
        }

        val linksFilmByProvider = providers.map {
          p =>
            Link(
              buildUrl(host, controllers.routes.TVContentController.topContentLeftByProvider(p.provider).url),
              Messages("tvcontent.top.type.provider.uri", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentNextByProvider(p.provider).url),
                Messages("tvcontent.next.provider.uri", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.currentContentByProvider(p.provider).url),
                Messages("tvcontent.all.now.type.provider.uri", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.allContentByTypeAndProvider("film", p.provider).url),
                Messages("tvcontent.today.type.provider.uri", "FILMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.currentContentByTypeAndProvider("film", p.provider).url),
                Messages("tvcontent.current.type.provider.uri", "FILMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentLeftByTypeAndProvider("film", p.provider).url),
                Messages("tvcontent.left.type.provider.uri", "FILMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentNextByTypeAndProvider("film", p.provider).url),
                Messages("tvcontent.next.type.provider.uri", "FILMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.allContentByTypeAndProvider("series", p.provider).url),
                Messages("tvcontent.today.type.provider.uri", "SERIES", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.currentContentByTypeAndProvider("series", p.provider).url),
                Messages("tvcontent.current.type.provider.uri", "SERIES", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentLeftByTypeAndProvider("series", p.provider).url),
                Messages("tvcontent.left.type.provider.uri", "SERIES", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentNextByTypeAndProvider("series", p.provider).url),
                Messages("tvcontent.next.type.provider.uri", "SERIES", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.allContentByTypeAndProvider("program", p.provider).url),
                Messages("tvcontent.today.type.provider.uri", "PROGRAMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.currentContentByTypeAndProvider("program", p.provider).url),
                Messages("tvcontent.current.type.provider.uri", "PROGRAMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentLeftByTypeAndProvider("program", p.provider).url),
                Messages("tvcontent.left.type.provider.uri", "PROGRAMS", p.provider)) ::
              Link(
                buildUrl(host, controllers.routes.TVContentController.contentNextByTypeAndProvider("program", p.provider).url),
                Messages("tvcontent.next.type.provider.uri", "PROGRAMS", p.provider)) :: Nil
        }

        val linkCategories: Seq[Link] = categories.map {
          c => Link(
            buildUrl(host, controllers.routes.TVChannelController.channelsByCategory(c.category).url),
            Messages("category.uri", c.category))
        }

        Seq(
          Link(
            buildUrl(host, controllers.routes.TVChannelController.channels().url),
            Messages("root.uri")),
          Link(
            buildUrl(host, controllers.routes.TVChannelProviderController.providers().url),
            Messages("provider.list.uri")),
          Link(
            buildUrl(host, controllers.routes.TVChannelCategoryController.categories().url),
            Messages("category.list.uri"))) ++
          linkProviders ++ linkCategories ++ linksFilmByProvider.flatten
    }

    links map (l => Ok(Json.toJson(l)))
  }
}

case class Link(uri: String, description: String)
