package utils

// TODO add a test
trait URLBuilder {
  def buildImageUrl(sub: String*) = sub.mkString + ".png"
  def buildImageName(name: String) =   name.replaceAll("\\s", "_").replaceAll("\\.","_").replaceAll("/","_")
  def buildUrl(sub: String*) = sub.mkString

}
