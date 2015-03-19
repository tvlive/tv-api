package acceptance

import cucumber.api.junit.Cucumber
import cucumber.api.junit.Cucumber.Options
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@Options(
  features = Array("features"),
  glue = Array("acceptance"),
  format = Array("pretty", "html:target/cucumber-report"),
  tags = Array()
)
class FeaturesRunner {
}