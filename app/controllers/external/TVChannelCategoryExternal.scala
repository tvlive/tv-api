package controllers.external

import models.{TVChannelProvider, TVChannelCategory}

case class TVChannelCategoryExternal(category: String)

object ChannelCategoryExternal {
  def apply(cat: TVChannelCategory) = TVChannelCategoryExternal(cat.category)
}


case class TVChannelProviderExternal(provider: String)

object ChannelProviderExternal{
  def apply(prov: TVChannelProvider) = TVChannelProviderExternal(prov.provider)
}