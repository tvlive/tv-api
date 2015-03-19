package configuration

import models._
import org.joda.time.{DateTimeZone, DateTime}
import play.api.Logger
import utils.TimeProvider

object ApplicationContext {

  implicit val conn: String => APIMongoConnection = {
    cn => new APIMongoConnection {
      override lazy val collectionName = cn
    }
  }

  implicit val time: TimeProvider = Environment.time.fold(new TimeProvider {
    override def currentDate(): DateTime = new DateTime(DateTimeZone.forID("UTC"))
  }
  )(t => {
    Logger.info(s"Setting the current time by configuration to $t")
    new TimeProvider {
      override def currentDate(): DateTime = t
    }
  }
    )
  val tvChannelCategoryRepository = new TVChannelCategoryRepository("tvChannelCategory")
  val tvChannelRepository = new TVChannelRepository("tvChannel")
  val tvChannelProviderReporitory = new TVChannelProviderRepository("tvChannelProvider")
  val tvContentRepository = new TVContentRepository("tvContent")
  val providerRepository = new TVChannelProviderRepository("tvChannelProvider")
  val categoryRepository = new TVChannelCategoryRepository("tvChannelCategory")
}
