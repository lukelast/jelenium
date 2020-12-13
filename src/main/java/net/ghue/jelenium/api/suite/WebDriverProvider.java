package net.ghue.jelenium.api.suite;

import java.io.Closeable;
import net.ghue.jelenium.api.config.JeleniumConfig;

public interface WebDriverProvider extends Closeable {

   /**
    * Cleanup all resources created. After this is called this object will never be used again.
    */
   @Override
   void close();

   WebDriverSession createWebDriver();

   void init( JeleniumConfig config );
}
