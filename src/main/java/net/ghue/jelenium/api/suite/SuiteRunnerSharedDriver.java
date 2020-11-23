package net.ghue.jelenium.api.suite;

import java.util.Collection;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.TestResultState;
import net.ghue.jelenium.api.config.JeleniumConfig;

/**
 * Run all tests through a single web driver instance.
 * 
 * @author Luke Last
 */
public abstract class SuiteRunnerSharedDriver extends SuiteRunnerBase {

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) {

      final WebDriverProvider driverProvider = new WebDriverProvider() {

         RemoteWebDriver webDriver = createWebDriver();

         @Override
         public void finished( JeleniumTestResult result, RemoteWebDriver driver ) {
            if ( result.getResult() == TestResultState.FAILED ) {
               driver.quit();
               webDriver = createWebDriver();
            }
         }

         @Override
         public RemoteWebDriver get() {
            return webDriver;
         }

      };

      for ( TestManager test : tests ) {
         test.runWithRetries( driverProvider );
      }

      driverProvider.get().quit();
   }
}
