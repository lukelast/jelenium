package net.ghue.jelenium.api;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * This manages the actual running of a test.
 * 
 * @author Luke Last
 */
public interface JeleniumTestRun {

   /**
    * @return The official name of this test.
    */
   TestName getName();

   TestResult getResult();

   void run( RemoteWebDriver remoteWebDriver );
}
