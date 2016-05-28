package net.ghue.jelenium.impl;

import javax.inject.Inject;
import org.openqa.selenium.WebDriver;

import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.TestArgs;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestLog;
import net.ghue.jelenium.api.WebNavigate;

final class TestContextImpl implements TestContext {

   private final TestLog log;

   private final TestArgs testArgs;

   private final WebDriver webDriver;

   private final WebNavigate webNavigate;

   @Inject
   public TestContextImpl( WebDriver webDriver, TestLog log, TestArgs testArgs,
                           WebNavigate webNavigate ) {
      this.webDriver = webDriver;
      this.log = log;
      this.testArgs = testArgs;
      this.webNavigate = webNavigate;
   }

   @Override
   public TestLog getLog() {
      return log;
   }

   @Override
   public TestArgs getTestArgs() {
      return this.testArgs;
   }

   @Override
   public HttpUrl getUrl() {
      return this.testArgs.getUrl();
   }

   @Override
   public WebDriver getWebDriver() {
      return webDriver;
   }

   @Override
   public WebNavigate getWebNavigate() {
      return this.webNavigate;
   }
}
