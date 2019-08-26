package net.ghue.jelenium.impl;

import java.util.List;
import javax.inject.Inject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestManager;

final class TestFactory {

   private final JeleniumSettings settings;

   private final List<TestManager> testsToRun;

   private final List<TestManager> testsSkipped;

   @Inject
   TestFactory( JeleniumSettings settings ) {
      this.settings = settings;

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

   public List<TestManager> getTestsToRun() {
      return this.testsToRun;
   }

   public List<TestManager> getSkippedTests() {
      return this.testsSkipped;
   }

   /**
    * Scan the class path to find all {@link JeleniumTest}'s to run and turn them in to
    * {@link TestManagerImpl}'s.
    */
   private List<TestManagerImpl> findTests() {
      final List<Class<? extends JeleniumTest>> testClasses = Scanner.findTests();

      final List<TestManagerImpl> tests =
            testClasses.stream()
                       .map( tc -> new TestManagerImpl( tc, settings ) )
                       .collect( ImmutableList.toImmutableList() );
      return tests;
   }

}
