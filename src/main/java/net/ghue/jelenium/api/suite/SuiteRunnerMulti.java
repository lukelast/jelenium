package net.ghue.jelenium.api.suite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;

public abstract class SuiteRunnerMulti implements JeleniumSuiteRunner {

   private final List<JeleniumSuiteRunner> subRunners = new ArrayList<>();

   private int threads = 4;

   protected void addSuiteRunner( JeleniumSuiteRunner runner ) {
      this.subRunners.add( runner );
   }

   public int getThreads() {
      return threads;
   }

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {
      ExecutorService exec = Executors.newFixedThreadPool( this.threads );

      for ( JeleniumSuiteRunner runner : subRunners ) {
         exec.execute( () -> {
            try {
               runner.runTests( tests, config );
            } catch ( Exception ex ) {
               throw new RuntimeException( ex );
            }
         } );
      }

      exec.shutdown();
      exec.awaitTermination( 12, TimeUnit.HOURS );
   }

   public void setThreads( int threads ) {
      this.threads = threads;
   }

}
