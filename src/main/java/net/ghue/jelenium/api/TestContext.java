package net.ghue.jelenium.api;

import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Provides everything that a test needs to interact with the world.
 *
 * @author Luke Last
 */
public interface TestContext {

   /**
    * @return A service to save browser screen shots as images.
    */
   ScreenshotSaver getScreenshotSaver();

   /**
    * @return The directory that all results for this test should be saved.
    */
   Path getResultDir();

   /**
    * @return The name of the test.
    */
   String getName();

   /**
    * Used for logging from a test.
    *
    * @return a {@link net.ghue.jelenium.api.TestLog} object.
    */
   TestLog getLog();

   /**
    * Global settings.
    *
    * @return a {@link net.ghue.jelenium.api.JeleniumSettings} object.
    */
   JeleniumSettings getSettings();

   /**
    * The primary URL as configured.
    *
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    * @see JeleniumSettings#getUrl()
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
    * @return a {@link net.ghue.jelenium.api.WebNavigate} object.
    */
   WebNavigate getWebNavigate();
}
