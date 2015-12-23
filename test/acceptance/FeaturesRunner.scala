package acceptance

import acceptance.support.Env
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.{AfterClass, BeforeClass}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("features"),
  glue = Array("acceptance"),
  format = Array("pretty", "html:target/cucumber-report"),
  tags = Array()
)
class FeaturesRunner

object FeatureSuite {

  private lazy val wireMockServer = new WireMockServer(wireMockConfig().port(Env.stubPort))

  private var isSetup = false

  def ensureSetup = if (!isSetup) setup()

  @BeforeClass
  def setup() {
    wireMockServer.start()
    WireMock.configureFor(Env.stubHost, Env.stubPort)
    isSetup = true
  }

  @AfterClass
  def cleanup() {
    wireMockServer.stop()
  }

}
