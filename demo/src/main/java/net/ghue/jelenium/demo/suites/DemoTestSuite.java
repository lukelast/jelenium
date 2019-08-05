package net.ghue.jelenium.demo.suites;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.suite.SuiteRunnerSharedDriver;

public class DemoTestSuite extends SuiteRunnerSharedDriver {

   @Override
   protected RemoteWebDriver createWebDriver() {
      return new ChromeDriver();
   }

}
