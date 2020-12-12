package net.ghue.jelenium.demo;

import java.util.concurrent.atomic.AtomicInteger;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;

/**
 * This demo should fail twice and then pass.
 */
public final class RetryDemo implements JeleniumTest {

   private static final int FAIL_RUNS = 2;

   /**
    * Static is the only way to keep state between test runs.
    */
   private static final AtomicInteger TEST_RUN_COUNTER = new AtomicInteger( 1 );

   @Override
   public int retries() {
      // 2 retries means at most 3 runs total.
      return 2;
   }

   @Override
   public void run( TestContext context ) throws Exception {
      final int currentRun = TEST_RUN_COUNTER.getAndIncrement();
      if ( currentRun <= FAIL_RUNS ) {
         fail( "Failing test" );
      }
   }

}
