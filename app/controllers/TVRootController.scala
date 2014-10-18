package controllers

import models.{ChannelCategoryRepository, ChannelProviderRepository, TVChannelCategoryRepository, TVChannelProviderRepository}
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TVRootController extends TVRootController {
  override val providerRepository = new TVChannelProviderRepository("tvChannelProvider")
  override val categoryRepository = new TVChannelCategoryRepository("tvChannelCategory")
}

trait TVRootController extends BaseController {

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
          p => Link(controllers.routes.TVChannelController.channelsByProvider(p.provider).url,
            Messages("provider.uri", p.provider))
        }

        val linkCategories: Seq[Link] = categories.map {
          c => Link(controllers.routes.TVChannelController.channelsByCategory(c.category).url,
            Messages("category.uri", c.category))
        }

        Seq(Link(controllers.routes.TVChannelController.channels().url, Messages("root.uri"))) ++
          Seq(Link(controllers.routes.TVChannelProviderController.providers().url, Messages("provider.list.uri"))) ++
          Seq(Link(controllers.routes.TVChannelCategoryController.categories().url, Messages("category.list.uri"))) ++
        linkProviders ++ linkCategories
    }

    links map (l => Ok(Json.toJson(l)))
  }
}

case class Link(uri: String, description: String)
