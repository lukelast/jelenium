package net.ghue.jelenium.impl.suite;

import java.util.Objects;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.suite.WebDriverSession;

public class WebDriverSessionBase implements WebDriverSession {

   protected final RemoteWebDriver driver;

   public WebDriverSessionBase( RemoteWebDriver driver ) {
      this.driver = Objects.requireNonNull( driver );
   }

   @Override
   public void close() {
      // https://stackoverflow.com/questions/15067107/difference-between-webdriver-dispose-close-and-quit
      this.driver.quit();
   }

   @Override
   public String getName() {
      if ( driver.getClass() == RemoteWebDriver.class ) {
         return "remote";
      }
      return driver.getClass().getSimpleName();
   }

   @Override
   public RemoteWebDriver getWebDriver() {
      return this.driver;
   }

}
