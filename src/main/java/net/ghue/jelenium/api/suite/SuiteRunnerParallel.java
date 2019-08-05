package net.ghue.jelenium.api.suite;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestRun;

public abstract class SuiteRunnerParallel extends SuiteRunnerBase {

   private int threads = 2;

   public int getThreads() {
      return threads;
   }

   @Override
   public void runTests( Collection<? extends JeleniumTestRun> tests ) throws Exception {
      ExecutorService exec = Executors.newFixedThreadPool( this.threads );
      for ( JeleniumTestRun test : tests ) {
         exec.execute( () -> {
            final RemoteWebDriver webDriver = this.createWebDriver();
            test.run( webDriver );
            webDriver.quit();
         } );
      }
      exec.shutdown();
      exec.awaitTermination( 2, TimeUnit.HOURS );
   }

   public void setThreads( int threads ) {
      this.threads = threads;
   }

}
