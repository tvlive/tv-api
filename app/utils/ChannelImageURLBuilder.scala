package utils

trait ChannelImageURLBuilder {
  def buildUrl(name: String) = s"http://localhost:9000/${name.replaceAll("\\s", "_")}.png"
}
