package net.ghue.jelenium.api.suite;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;

public abstract class SuiteRunnerParallel extends SuiteRunnerBase {

   private int threads = 2;

   public int getThreads() {
      return threads;
   }

   @Override
   protected WebDriverProvider createProvider() {
      return new WebDriverProvider() {

         @Override
         public void finished( JeleniumTestResult result, RemoteWebDriver driver ) {
            driver.quit();
         }

         @Override
         public RemoteWebDriver get() {
            return createWebDriver();
         }

      };
   }

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {
      ExecutorService exec = Executors.newFixedThreadPool( this.threads );
      for ( TestManager test : tests ) {
         exec.execute( () -> {
            test.runWithRetries( createProvider() );
         } );
      }
      exec.shutdown();
      exec.awaitTermination( 12, TimeUnit.HOURS );
   }

   public void setThreads( int threads ) {
      this.threads = threads;
   }

}
