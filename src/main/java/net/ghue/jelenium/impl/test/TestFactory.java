package net.ghue.jelenium.impl.test;

import java.util.List;
import javax.inject.Inject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.TestManager;
import net.ghue.jelenium.api.test.JeleniumTest;

/**
 * Scan for and collect all tests to run and check which should be skipped.
 */
final class TestFactory {

   private final JeleniumConfig config;

   private final Scanner scanner;

   private final List<TestManager> testsSkipped;

   private final List<TestManager> testsToRun;

   @Inject
   TestFactory( JeleniumConfig config, Scanner scanner ) {
      this.config = config;
      this.scanner = scanner;

      final List<TestManagerImpl> testManagers = findTests();

      final Builder<TestManager> toRun = ImmutableList.builder();
      final Builder<TestManager> skipped = ImmutableList.builder();

      for ( TestManagerImpl tm : testManagers ) {
         if ( tm.isSkipped() ) {
            skipped.add( tm );
         } else {
            toRun.add( tm );
         }
      }
      this.testsToRun = toRun.build();
      this.testsSkipped = skipped.build();
   }

   /**
    * Scan the class path to find all {@link JeleniumTest}'s to run and turn them in to
    * {@link TestManagerImpl}'s.
    */
   private List<TestManagerImpl> findTests() {
      final List<TestManagerImpl> tests =
            this.scanner.findTests()
                        .map( tc -> new TestManagerImpl( tc, config ) )
                        .collect( ImmutableList.toImmutableList() );
      return tests;
   }

   public List<TestManager> getSkippedTests() {
      return this.testsSkipped;
   }

   public List<TestManager> getTestsToRun() {
      return this.testsToRun;
   }

}
