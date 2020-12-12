package net.ghue.jelenium.impl;

import static net.ghue.jelenium.api.test.TestResultState.*;
import java.util.Collection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.ghue.jelenium.api.ex.TestErrorException;
import net.ghue.jelenium.api.ex.TestFailedException;
import net.ghue.jelenium.api.suite.TestResultsHandler;
import net.ghue.jelenium.api.test.JeleniumTestResult;
import net.ghue.jelenium.api.test.TestResultState;

public class ResultsHandlerStdOut implements TestResultsHandler {

   private void appendResults( StringBuilder sb,
                               Multimap<TestResultState, JeleniumTestResult> results,
                               TestResultState state,
                               String name ) {
      if ( !results.get( state ).isEmpty() ) {
         sb.append( "##### " ).append( name ).append( ":" ).append( '\n' );
         for ( JeleniumTestResult testResult : results.get( state ) ) {
            sb.append( "  " ).append( testResult.getName().getShortName() );
            if ( !testResult.getWebDriverName().isEmpty() ) {
               sb.append( " (" ).append( testResult.getWebDriverName() ).append( ")" );
            }
            sb.append( '\n' );
         }
         sb.append( '\n' );
      }
   }

   private Multimap<TestResultState, JeleniumTestResult> binResults( Collection<JeleniumTestResult> testResults ) {
      Builder<TestResultState, JeleniumTestResult> builder = ImmutableMultimap.builder();
      for ( JeleniumTestResult tr : testResults ) {
         builder.put( tr.getResult(), tr );
      }
      return builder.build();
   }

   @Override
   public void processResults( Collection<JeleniumTestResult> testResults ) {
      final Multimap<TestResultState, JeleniumTestResult> results = binResults( testResults );
      final StringBuilder sb = new StringBuilder( 64 );

      sb.append( '\n' )
        .append( "==================== FINISHED ====================" )
        .append( '\n' );

      sb.append( "Total Test Results: " ).append( results.size() ).append( '\n' );
      sb.append( "Skipped: " ).append( results.keys().count( SKIPPED ) ).append( '\n' );
      sb.append( "Passed: " ).append( results.keys().count( PASSED ) ).append( '\n' );
      sb.append( "Failed-Retried: " )
        .append( results.keys().count( FAILED_RETRIED ) )
        .append( '\n' );
      sb.append( "Failed: " ).append( results.keys().count( FAILED ) ).append( '\n' );
      sb.append( "Errored: " ).append( results.keys().count( ERROR ) ).append( '\n' );

      sb.append( '\n' );

      appendResults( sb, results, SKIPPED, "Skipped" );
      appendResults( sb, results, PASSED, "Passed" );
      appendResults( sb, results, FAILED_RETRIED, "Failed-Retried" );
      appendResults( sb, results, FAILED, "Failed" );
      appendResults( sb, results, ERROR, "Errored" );

      System.out.println( sb.toString() );

      if ( 0 < results.keys().count( ERROR ) ) {
         throw new TestErrorException( results.keys().count( ERROR ) + " tests had an ERROR" );
      }

      if ( 0 < results.keys().count( FAILED ) ) {
         throw new TestFailedException( results.keys().count( FAILED ) + " tests failed" );
      }
   }

}
