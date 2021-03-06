package net.ghue.jelenium.api.test;

import java.nio.file.Path;
import java.time.Duration;
import javax.annotation.Nullable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import net.ghue.jelenium.api.action.ActionFactory;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.log.TestLog;
import net.ghue.jelenium.api.page.WebNavigate;
import okhttp3.HttpUrl;

/**
 * Provides everything that a test needs to interact with the world.
 *
 * @author Luke Last
 */
public interface TestContext {

   ActionFactory action();

   /**
    * Global settings.
    *
    * @return a {@link net.ghue.jelenium.api.config.JeleniumConfig} object.
    */
   JeleniumConfig getConfig();

   /**
    * @return The name of the test.
    */
   TestName getName();

   /**
    * @return The directory that all results for this test should be saved.
    */
   Path getResultDir();

   /**
    * @return A service to save browser screen shots as images.
    */
   ScreenshotSaver getScreenshotSaver();

   /**
    * The primary URL as configured.
    *
    * @return a {@link HttpUrl} object.
    * @see JeleniumConfig#url()
    */
   HttpUrl getUrl();

   /**
    * The {@link WebDriver} instance to be used for the test.
    *
    * @return a {@link org.openqa.selenium.WebDriver} object.
    */
   RemoteWebDriver getWebDriver();

   /**
    * Used to navigate the {@link WebDriver} to different URL's.
    *
    * @return a {@link net.ghue.jelenium.api.page.WebNavigate} object.
    */
   WebNavigate getWebNavigate();

   /**
    * Used for logging from a test.
    *
    * @return a {@link net.ghue.jelenium.api.log.TestLog} object.
    */
   TestLog log();

   /**
    * Fixed pauses are bad, don't do it. But if you must, at least this will log it.
    * 
    * @param pauseDuration Time to sleep.
    */
   default void pause( @Nullable Duration pauseDuration ) {
      if ( pauseDuration == null ) {
         throw new IllegalArgumentException( "Duration must not be null" );
      }
      if ( pauseDuration.isNegative() ) {
         throw new IllegalArgumentException( "Pause duration must be positive" );
      }
      log().info().msg( "Pausing for %s", pauseDuration ).log();
      try {
         Thread.sleep( pauseDuration.toMillis() );
      } catch ( InterruptedException iex ) {
         throw new RuntimeException( iex );
      }
   }

   default WebDriverWait webDriverWait() {
      return new WebDriverWait( getWebDriver(),
                                getConfig().retryTimeout().toMillis(),
                                getConfig().retryDelay().toMillis() );
   }
}
