package utils

trait URLBuilder {
  def buildImageUrl(name: String) = s"http://localhost:9000/${name.replaceAll("\\s", "_")}.png"
  def buildUrl(name: String) = s"http://localhost:9000$name"

}
