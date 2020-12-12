package net.ghue.jelenium.api.suite;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.ghue.jelenium.api.config.JeleniumConfig;

public class SuiteRunnerDefault implements JeleniumSuiteRunner {

   protected WebDriverProvider createWdp( JeleniumConfig config ) {
      Class<WebDriverProvider> fromConfig = config.suiteWdp();
      if ( fromConfig != null ) {
         try {
            return fromConfig.newInstance();
         } catch ( InstantiationException | IllegalAccessException ex ) {
            throw new RuntimeException( ex );
         }
      } else {
         return new WdpLocalBrowser();
      }
   }

   protected int getThreads( JeleniumConfig config ) {
      return config.suiteThreads();
   }

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {

      final int threads = this.getThreads( config );

      final ExecutorService exec = Executors.newFixedThreadPool( threads );

      final Queue<TestManager> testQueue = new ConcurrentLinkedQueue<>( tests );

      for ( int i = 0; i < threads; i++ ) {
         final WebDriverProvider wdp = this.createWdp( config );
         exec.execute( new QueueRunner( config, wdp, testQueue ) );
      }
      exec.shutdown();
      exec.awaitTermination( 12, TimeUnit.HOURS );
   }

}
