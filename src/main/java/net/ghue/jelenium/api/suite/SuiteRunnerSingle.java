package net.ghue.jelenium.api.suite;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.ex.TestErrorException;

/**
 * Runs one and only one test.
 */
public class SuiteRunnerSingle implements JeleniumSuiteRunner {

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {

      final Optional<TestManager> maybeTestToRun = tests.stream().findFirst();

      if ( !maybeTestToRun.isPresent() ) {
         throw new TestErrorException( "No tests found" );
      }

      final TestManager testToRun = maybeTestToRun.get();
      final Queue<TestManager> queue = new LinkedList<>();
      queue.add( testToRun );

      final WdpLocalBrowser wdp = new WdpLocalBrowser();
      new QueueRunner( config, wdp, queue ).run();

   }

}
