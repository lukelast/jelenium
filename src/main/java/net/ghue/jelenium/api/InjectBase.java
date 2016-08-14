package net.ghue.jelenium.api;

import javax.inject.Inject;
import org.openqa.selenium.WebDriver;

public abstract class InjectBase {

   @Inject
   private TestContext context;

   @Inject
   private WebDriver driver;

   @Inject
   private TestLog log;

   @Inject
   private HttpUrl url;

   public TestContext getContext() {
      return context;
   }

   public WebDriver getDriver() {
      return driver;
   }

   public HttpUrl getUrl() {
      return url;
   }

   public TestLog log() {
      return log;
   }

}
