package net.ghue.jelenium.api;

import org.openqa.selenium.WebDriver;

/**
 * <p>
 * TestContext interface.
 * </p>
 *
 * @author Luke Last
 */
public interface TestContext {

   /**
    * <p>
    * getLog.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.TestLog} object.
    */
   TestLog getLog();

   /**
    * <p>
    * getTestArgs.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.TestArgs} object.
    */
   TestArgs getTestArgs();

   /**
    * <p>
    * getUrl.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    */
   HttpUrl getUrl();

   /**
    * <p>
    * getWebDriver.
    * </p>
    *
    * @return a {@link org.openqa.selenium.WebDriver} object.
    */
   WebDriver getWebDriver();

   /**
    * <p>
    * getWebNavigate.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.WebNavigate} object.
    */
   WebNavigate getWebNavigate();
}
