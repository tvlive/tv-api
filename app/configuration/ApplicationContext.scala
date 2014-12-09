package configuration

import models._

object ApplicationContext {

  implicit val conn : String => APIMongoConnection = {
    cn => new APIMongoConnection {
      override lazy val collectionName = cn
    }
  }

  val tvChannelCategoryRepository = new TVChannelCategoryRepository("tvChannelCategory")
  val tvChannelRepository = new TVChannelRepository("tvChannel")
  val tvChannelProviderReporitory = new TVChannelProviderRepository("tvChannelProvider")
  val tvContentRepository = new TVContentRepository("tvContent")
  val providerRepository = new TVChannelProviderRepository("tvChannelProvider")
  val categoryRepository = new TVChannelCategoryRepository("tvChannelCategory")
}
