package net.ghue.jelenium.api.suite;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTestRun;
import net.ghue.jelenium.api.TestResult;
import net.ghue.jelenium.api.ex.TestErrorException;
import net.ghue.jelenium.api.ex.TestFailedException;

public abstract class SuiteRunnerBase implements JeleniumSuiteRunner {

   private JeleniumSettings settings;

   @Nonnull
   protected abstract RemoteWebDriver createWebDriver();

   @Override
   public JeleniumSettings getSettings() {
      return this.settings;
   }

   @Override
   public void processResults( Collection<? extends JeleniumTestRun> tests ) throws Exception {
      final Multiset<TestResult> results = tests.stream()
                                                .map( JeleniumTestRun::getResult )
                                                .collect( ImmutableMultiset.toImmutableMultiset() );
      final StringBuilder sb = new StringBuilder( 64 );

      sb.append( '\n' ).append( "========== PASSED ==========" ).append( '\n' );

      sb.append( "Total Tests: " ).append( results.size() ).append( '\n' );
      sb.append( "Passed: " ).append( results.count( TestResult.PASSED ) ).append( '\n' );
      sb.append( "Failed: " ).append( results.count( TestResult.FAILED ) ).append( '\n' );
      sb.append( "Errored: " ).append( results.count( TestResult.ERROR ) ).append( '\n' );
      sb.append( "Skipped: " ).append( results.count( TestResult.SKIPPED ) ).append( '\n' );

      System.out.println( sb.toString() );

      if ( 0 < results.count( TestResult.ERROR ) ) {
         throw new TestErrorException( results.count( TestResult.ERROR ) + " tests had an ERROR" );
      }

      if ( 0 < results.count( TestResult.FAILED ) ) {
         throw new TestFailedException( results.count( TestResult.FAILED ) + " tests failed" );
      }
   }

   @Override
   public void setSettings( JeleniumSettings settings ) {
      this.settings = settings;
   }
}
