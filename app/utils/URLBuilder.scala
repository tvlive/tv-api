package utils

// TODO add a test
trait URLBuilder {
  def buildImageUrl(sub: String*) = sub.mkString.replaceAll("\\s", "_") + ".png"
  def buildUrl(sub: String*) = sub.mkString

}
