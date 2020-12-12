package net.ghue.jelenium.demo.skip;

import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;

/**
 * This example demonstrates how to mark a test to always be skipped.
 */
public final class AlwaysSkip implements JeleniumTest {

   @Override
   public void run( TestContext context ) throws Exception {}

   /**
    * This method is checked before the test is run.
    */
   @Override
   public boolean skipTest() {
      return true;
   }
}
