package controllers

import models.{TVChannelCategoryRepository, TVChannelProviderRepository, ChannelCategoryRepository, ChannelProviderRepository}
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

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

    val links = r.map {
      case (providers, categories) =>
        Seq(controllers.routes.TVChannelController.channels().url) ++
          providers.map(p => controllers.routes.TVChannelController.channelsByProvider(p.provider).url) ++
          categories.map(c => controllers.routes.TVChannelController.channelsByCategory(c.category).url)
    }
    links map (l => Ok(Json.toJson(l)))
  }
}
