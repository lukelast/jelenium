package net.ghue.jelenium.api;

import org.openqa.selenium.WebDriver;
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

   /**
    * This life-cycle callback gives you a chance to modify {@link WebDriver} {@link Options} before
    * the test starts.
    * 
    * @param options {@link WebDriver} options.
    */
   default void manage( Options options ) {}

   /**
    * This life-cycle event is executed before the main test {@link #run(TestContext)}.
    * 
    * @param context The context of the test being executed.
    * @throws Exception
    */
   default void onBeforeRun( TestContext context ) throws Exception {}

   /**
    * Life-cycle event run after test execution if the test failed.
    * 
    * @param context
    * @throws Exception
    */
   default void onFail( TestContext context ) throws Exception {}

   /**
    * Called after the test finishes no matter if it passed or failed. It will not be called if the
    * test was skipped.
    * 
    * @param context
    * @throws Exception
    */
   default void onFinish( TestContext context ) throws Exception {}

   /**
    * Life-cycle event run after test execution if the test passed.
    * 
    * @param context
    * @throws Exception
    */
   default void onPass( TestContext context ) throws Exception {}

   /**
    * Beta.
    * 
    * @return The desired number of test retries.
    */
   default int retries() {
      return -1;
   }

   void run( TestContext context ) throws Exception;;

   /**
    * Beta.
    * 
    * @return {@code true} to avoid running this test.
    */
   default boolean skipTest() {
      return false;
   }

}
