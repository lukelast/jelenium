package net.ghue.jelenium.impl;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.suite.SuiteRunnerSharedDriver;

public final class DefaultTestSuite extends SuiteRunnerSharedDriver {

   @Override
   protected RemoteWebDriver createWebDriver() {
      return new ChromeDriver();
   }

}
