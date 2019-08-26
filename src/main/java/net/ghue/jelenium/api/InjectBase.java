package net.ghue.jelenium.api;

import javax.inject.Inject;
import org.openqa.selenium.WebDriver;
import net.ghue.jelenium.api.log.TestLog;

/**
 * A base class used to inject a bunch of services.
 * 
 * @author Luke Last
 */
public abstract class InjectBase {

   @Inject
   private TestContext context;

   @Inject
   private WebDriver driver;

   @Inject
   private TestLog log;

   @Inject
   private HttpUrl url;

   protected TestContext getContext() {
      return context;
   }

   protected WebDriver getDriver() {
      return driver;
   }

   protected HttpUrl getUrl() {
      return url;
   }

   protected TestLog log() {
      return log;
   }

}
