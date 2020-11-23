package net.ghue.jelenium.api.suite;

import java.util.Queue;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public interface Worker {

   void init();

   default void work( Queue<TestManager> tms ) {
      while ( true ) {
         final TestManager tm = tms.poll();
         if ( tm == null ) {
            return;
         }
         this.runWithRetries( tm );
      }
   }

   void finish();

   default void runWithRetries( TestManager tm ) {
      // Use a max attempts limit to prevent running forever.
      for ( int attempt = 1; attempt < 10; attempt++ ) {
         final JeleniumTestResult result = this.run( tm, attempt );
         if ( result.shouldTryAgain( attempt ) ) {
            result.setRetried( true );
         } else {
            break;// DONE
         }
      }
   }

   JeleniumTestResult run( TestManager tm, int attempt );
}
