package net.ghue.jelenium.impl;

import static net.ghue.jelenium.api.TestResultState.*;
import java.util.Collection;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestResultState;
import net.ghue.jelenium.api.TestResultsHandler;
import net.ghue.jelenium.api.ex.TestErrorException;
import net.ghue.jelenium.api.ex.TestFailedException;

public class ResultsHandlerStdOut implements TestResultsHandler {

   @Override
   public void processResults( Collection<JeleniumTestResult> testResults ) {
      final Multiset<TestResultState> results =
            testResults.stream()
                       .map( JeleniumTestResult::getResult )
                       .collect( ImmutableMultiset.toImmutableMultiset() );
      final StringBuilder sb = new StringBuilder( 64 );

      sb.append( '\n' ).append( "========== FINISHED ==========" ).append( '\n' );

      sb.append( "Total Test Results: " ).append( results.size() ).append( '\n' );
      sb.append( "Passed: " ).append( results.count( PASSED ) ).append( '\n' );
      sb.append( "Failed-Retried: " ).append( results.count( FAILED_RETRIED ) ).append( '\n' );
      sb.append( "Failed: " ).append( results.count( FAILED ) ).append( '\n' );
      sb.append( "Errored: " ).append( results.count( ERROR ) ).append( '\n' );
      sb.append( "Skipped: " ).append( results.count( SKIPPED ) ).append( '\n' );

      System.out.println( sb.toString() );

      if ( 0 < results.count( ERROR ) ) {
         throw new TestErrorException( results.count( ERROR ) + " tests had an ERROR" );
      }

      if ( 0 < results.count( FAILED ) ) {
         throw new TestFailedException( results.count( FAILED ) + " tests failed" );
      }
   }

}
