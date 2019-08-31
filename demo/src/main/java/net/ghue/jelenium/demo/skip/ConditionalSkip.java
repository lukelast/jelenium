package net.ghue.jelenium.demo.skip;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.ex.SkipTestException;

/**
 * Demonstrate how to conditionally skip a test at any time during its run.
 */
public final class ConditionalSkip implements JeleniumTest {

   @Override
   public void run( TestContext context ) throws Exception {
      if ( !context.getConfig().is( "some-key", "some-value" ) ) {
         throw new SkipTestException( "reason for skip" );
      }
   }

}
