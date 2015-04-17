package acceptance

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("features"),
  glue = Array("acceptance"),
  format = Array("pretty", "html:target/cucumber-report"),
  tags = Array()
)
class FeaturesRunner {
}