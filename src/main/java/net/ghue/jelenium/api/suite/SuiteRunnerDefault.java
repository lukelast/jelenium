package net.ghue.jelenium.api.suite;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.impl.Utils;

public class SuiteRunnerDefault implements JeleniumSuiteRunner {

   protected WebDriverProvider createWdp( JeleniumConfig config ) {
      Class<WebDriverProvider> fromConfig = config.suiteWdp();
      if ( fromConfig != null ) {
         return Utils.newInstance( fromConfig );
      } else {
         return new WdpLocalBrowser(); // Default.
      }
   }

   protected ExecutorService getExecutor( JeleniumConfig config ) {
      final int threads = this.getThreads( config );
      final ExecutorService exec = Executors.newFixedThreadPool( threads );
      return exec;
   }

   protected int getThreads( JeleniumConfig config ) {
      return config.suiteThreads();
   }

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {
      final ExecutorService exec = this.getExecutor( config );
      final Queue<TestManager> testQueue = new ConcurrentLinkedQueue<>( tests );
      final int threads = this.getThreads( config );

      for ( int i = 0; i < threads; i++ ) {
         final WebDriverProvider wdp = this.createWdp( config );
         exec.execute( new QueueRunner( config, wdp, testQueue ) );
      }
      exec.shutdown();
      exec.awaitTermination( 12, TimeUnit.HOURS );
   }

}
