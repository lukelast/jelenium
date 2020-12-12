package net.ghue.jelenium.api.suite;

import java.util.Queue;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;

/**
 * This must be executed on the same thread.
 */
public final class QueueRunner implements Runnable {

   private final WebDriverProvider wdp;

   private final Queue<TestManager> testQueue;

   private final JeleniumConfig config;

   QueueRunner( JeleniumConfig config, WebDriverProvider wdp, Queue<TestManager> testQueue ) {
      this.wdp = wdp;
      this.testQueue = testQueue;
      this.config = config;
   }

   @Override
   public void run() {
      try {
         this.wdp.init( this.config );
         this.work( testQueue );
      } finally {
         if ( this.webDriver != null ) {
            this.wdp.destroyWebDriver( this.webDriver );
            this.webDriver = null;
         }
         this.wdp.close();
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

   private RemoteWebDriver webDriver;

   private RemoteWebDriver getWebDriver() {
      if ( this.webDriver == null ) {
         this.webDriver = this.wdp.createWebDriver();
         // Give the browser time to start up.
         // Sending commands too soon has caused problems.
         try {
            Thread.sleep( 1_000 );
         } catch ( InterruptedException ex ) {}
      }
      return this.webDriver;
   }

   private void finishWebDriver() {
      if ( !this.config.suiteReuseBrowser() && this.webDriver != null ) {
         this.wdp.destroyWebDriver( this.webDriver );
         this.webDriver = null;
      }
   }

   void runWithRetries( TestManager tm ) {
      // Use a max attempts limit to prevent running forever.
      for ( int attempt = 1; attempt < 10; attempt++ ) {
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

}
