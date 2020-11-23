package net.ghue.jelenium.demo.suites;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.suite.SuiteRunnerSharedDriver;
import net.ghue.jelenium.api.suite.WebDriverProvider;

public class DemoTestSuite extends SuiteRunnerSharedDriver {

   @Override
   protected RemoteWebDriver createWebDriver() {
      return new ChromeDriver();
   }

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
