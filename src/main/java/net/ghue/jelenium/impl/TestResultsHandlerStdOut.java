package net.ghue.jelenium.impl;

import java.util.Collection;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestResultState;
import net.ghue.jelenium.api.TestResultsHandler;
import net.ghue.jelenium.api.ex.TestErrorException;
import net.ghue.jelenium.api.ex.TestFailedException;

public class TestResultsHandlerStdOut implements TestResultsHandler {

   @Override
   public void processResults( Collection<JeleniumTestResult> testResults ) {
      final Multiset<TestResultState> results =
            testResults.stream()
                       .map( JeleniumTestResult::getResult )
                       .collect( ImmutableMultiset.toImmutableMultiset() );
      final StringBuilder sb = new StringBuilder( 64 );

      sb.append( '\n' ).append( "========== PASSED ==========" ).append( '\n' );

      sb.append( "Total Tests: " ).append( results.size() ).append( '\n' );
      sb.append( "Passed: " ).append( results.count( TestResultState.PASSED ) ).append( '\n' );
      sb.append( "Failed: " ).append( results.count( TestResultState.FAILED ) ).append( '\n' );
      sb.append( "Errored: " ).append( results.count( TestResultState.ERROR ) ).append( '\n' );
      sb.append( "Skipped: " ).append( results.count( TestResultState.SKIPPED ) ).append( '\n' );

      System.out.println( sb.toString() );

      if ( 0 < results.count( TestResultState.ERROR ) ) {
         throw new TestErrorException( results.count( TestResultState.ERROR ) +
                                       " tests had an ERROR" );
      }

      if ( 0 < results.count( TestResultState.FAILED ) ) {
         throw new TestFailedException( results.count( TestResultState.FAILED ) + " tests failed" );
      }
   }

}
