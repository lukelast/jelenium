package net.ghue.jelenium.demo;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

/**
 * Basic example of a failing test.
 */
public final class FailingTestDemo implements JeleniumTest {

   @Override
   public int retries() {
      // Enable a retry, should be 2 runs total.
      return 1;
   }

   @Override
   public void run( TestContext context ) throws Exception {
      // Throwing any exception fails the test.
      throw new RuntimeException( "fail" );

      // There is also a helper method to throw an exception.
      //fail( "fail" );
   }

   /**
    * Skip this one by default so all the tests pass.
    */
   @Override
   public boolean skipTest() {
      return true;
   }

}
