package net.ghue.jelenium.api.suite;

import java.io.Closeable;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.config.JeleniumConfig;

public interface WebDriverProvider extends Closeable {

   /**
    * Cleanup all resources created. After this is called this object will never be used again.
    */
   @Override
   void close();

   RemoteWebDriver createWebDriver();

   default void destroyWebDriver( RemoteWebDriver wd ) {
      wd.quit();
   }

   void init( JeleniumConfig config );
}
