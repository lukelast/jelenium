package net.ghue.jelenium.api;

import org.openqa.selenium.WebDriver.Options;

/**
 * <p>
 * Every non-abstract class that implements this interface becomes a test.
 * </p>
 *
 * @author Luke Last
 */
public interface SeleniumTest {

   default void fail( String reason ) throws Exception {
      throw new Exception( reason );
   }

   default void manage( Options options ) {}

   default void onFail( TestContext context ) throws Exception {}

   default void onFinish( TestContext context ) throws Exception {}

   default void onPass( TestContext context ) throws Exception {}

   default int retries() {
      return -1;
   }

   void run( TestContext context ) throws Exception;

}
