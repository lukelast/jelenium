package net.ghue.jelenium.api.suite;

import java.util.Collection;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.TestManager;

/**
 * Run all tests through a single web driver instance.
 * 
 * @author Luke Last
 */
public abstract class SuiteRunnerSharedDriver extends SuiteRunnerBase {

   @Override
   public void runTests( Collection<TestManager> tests ) {
      final RemoteWebDriver webDriver = this.createWebDriver();
      for ( TestManager test : tests ) {
         test.run( webDriver );
      }
      webDriver.quit();
   }
}
