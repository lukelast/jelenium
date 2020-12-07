package net.ghue.jelenium.api.suite;

import java.util.Queue;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public final class QueueRunner implements Runnable {

   private final Worker worker;

   private final Queue<TestManager> testQueue;

   QueueRunner( Worker worker, Queue<TestManager> testQueue ) {
      this.worker = worker;
      this.testQueue = testQueue;
   }

   @Override
   public void run() {
      worker.init();
      this.work( testQueue );
      worker.finish();
   }

   void work( Queue<TestManager> tms ) {
      while ( true ) {
         final TestManager tm = tms.poll();
         if ( tm == null ) {
            return;
         }
         this.runWithRetries( tm );
      }
   }

   void runWithRetries( TestManager tm ) {
      // Use a max attempts limit to prevent running forever.
      for ( int attempt = 1; attempt < 10; attempt++ ) {
         final JeleniumTestResult result = this.worker.run( tm, attempt );
         if ( result.shouldTryAgain( attempt ) ) {
            result.setRetried( true );
         } else {
            break;// DONE
         }
      }
   }

}
