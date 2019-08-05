package net.ghue.jelenium.api.suite;

import java.util.Collection;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestRun;

/**
 * Run all tests through a single web driver instance.
 * 
 * @author Luke Last
 */
public abstract class SuiteRunnerSharedDriver extends SuiteRunnerBase {

   @Override
   public void runTests( Collection<? extends JeleniumTestRun> tests ) {
      final RemoteWebDriver webDriver = this.createWebDriver();
      for ( JeleniumTestRun test : tests ) {
         test.run( webDriver );
      }
      webDriver.quit();
   }
}
