package net.ghue.jelenium.demo;

import java.util.Collection;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestRun;
import net.ghue.jelenium.api.suite.SuiteRunnerBase;

public final class SampleSuite extends SuiteRunnerBase {

   @Override
   protected RemoteWebDriver createWebDriver() {
      return new ChromeDriver();
   }

   @Override
   public void runTests( Collection<? extends JeleniumTestRun> tests ) throws Exception {}
}
