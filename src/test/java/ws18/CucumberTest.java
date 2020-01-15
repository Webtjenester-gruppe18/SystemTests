package ws18;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features", snippets = CucumberOptions.SnippetType.CAMELCASE)
public class CucumberTest {
}
