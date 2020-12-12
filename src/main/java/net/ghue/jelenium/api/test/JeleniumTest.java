package net.ghue.jelenium.api.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import net.ghue.jelenium.api.ex.SkipTestException;

/**
 * <p>
 * Every non-abstract class that implements this interface becomes a test.
 * </p>
 *
 * @author Luke Last
 */
public interface JeleniumTest {

   /**
    * Throws an exception to skip the currently running test.
    * 
    * @param reason Why is the test being skipped.
    * @throws SkipTestException
    */
   static void skip( String reason ) throws SkipTestException {
      throw new SkipTestException( reason );
   }

   /**
    * Force an exception to fail the test.
    * 
    * @param reason Why is this test failing.
    * @throws Exception
    */
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
    * @throws Exception If error occurs.
    */
   default void onBeforeRun( TestContext context ) throws Exception {}

   /**
    * Life-cycle event run after test execution if the test failed.
    * 
    * @param context The context of the test being executed.
    * @throws Exception If error occurs.
    */
   default void onFail( TestContext context ) throws Exception {}

   /**
    * Called after the test finishes no matter if it passed or failed. It will not be called if the
    * test was skipped.
    * 
    * @param context The context of the test being executed.
    * @param result The final result of the test.
    * @throws Exception If error occurs.
    */
   default void onFinish( TestContext context, TestResultState result ) throws Exception {}

   /**
    * Life-cycle event run after test execution if the test passed.
    * 
    * @param context The context of the test being executed.
    * @throws Exception If error occurs.
    */
   default void onPass( TestContext context ) throws Exception {}

   /**
    * Beta.
    * 
    * @return The desired number of test retries. A value less than 0 means use the global default.
    */
   default int retries() {
      return -1;
   }

   void run( TestContext context ) throws Exception;

   /**
    * Beta.
    * 
    * @return {@code true} to avoid running this test.
    */
   default boolean skipTest() {
      return false;
   }

}
