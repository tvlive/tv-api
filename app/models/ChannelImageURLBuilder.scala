package models

trait ChannelImageURLBuilder {
  def buildUrl(name: String) = s"/${name.replaceAll("\\s", "_")}.png"
}
