package net.ghue.jelenium.api.suite;

import java.util.Queue;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.test.JeleniumTestResult;
import net.ghue.jelenium.impl.Utils;

/**
 * This must be executed on the same thread.
 */
public final class QueueRunner implements Runnable {

   private static final int RETRY_LIMIT = 10;

   private final JeleniumConfig config;

   private final Queue<TestManager> testQueue;

   private final WebDriverProvider wdp;

   private WebDriverSession webDriver;

   QueueRunner( JeleniumConfig config, WebDriverProvider wdp, Queue<TestManager> testQueue ) {
      this.wdp = wdp;
      this.testQueue = testQueue;
      this.config = config;
   }

   private void finishWebDriver() {
      if ( !this.config.suiteReuseBrowser() && this.webDriver != null ) {
         this.webDriver.close();
         this.webDriver = null;
      }
   }

   private WebDriverSession getWebDriver() {
      if ( this.webDriver == null ) {
         this.webDriver = this.wdp.createWebDriver();
         // Give the browser time to start up.
         // Sending commands too soon has caused problems.
         Utils.sleep( 1 );
      }
      return this.webDriver;
   }

   @Override
   public void run() {
      try {
         this.wdp.init( this.config );
         this.work( testQueue );
      } finally {
         if ( this.webDriver != null ) {
            this.webDriver.close();
            this.webDriver = null;
         }
         this.wdp.close();
      }
   }

   void runWithRetries( TestManager tm ) {
      // Use a max attempts limit to prevent running forever.
      for ( int attempt = 1; attempt < RETRY_LIMIT; attempt++ ) {
         final JeleniumTestResult result;
         try {
            result = tm.run( getWebDriver(), attempt );
         } finally {
            this.finishWebDriver();
         }
         if ( result.shouldTryAgain( attempt ) ) {
            result.setRetried( true );
         } else {
            break;// DONE
         }
      }
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

}
