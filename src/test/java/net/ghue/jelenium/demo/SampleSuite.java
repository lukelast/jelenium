package net.ghue.jelenium.demo;

import java.util.Collection;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.SuiteRunnerBase;
import net.ghue.jelenium.api.suite.WebDriverProvider;

public final class SampleSuite extends SuiteRunnerBase {

   @Override
   protected RemoteWebDriver createWebDriver() {
      //return new ChromeDriver();
      throw new RuntimeException( "not supported" );
   }

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {}

   @Override
   protected WebDriverProvider createProvider() {
      return new WebDriverProvider() {

         @Override
         public RemoteWebDriver get() {
            return createWebDriver();
         }

         @Override
         public void finished( JeleniumTestResult result, RemoteWebDriver driver ) {
            driver.quit();
         }
      };
   }
}
