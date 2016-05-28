package net.ghue.jelenium;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.impl.TestRunnerImpl;

public final class Runner {

   public static void main( String[] args ) throws Exception {

      Map<String, String> parsedArgs = parseMainArgs( args );
      System.out.println( "Test Arguments: " );
      for ( Entry<String, String> arg : parsedArgs.entrySet() ) {
         System.out.println( arg.getKey() + " = " + arg.getValue() );
      }
      System.out.println();

      final TestRunnerImpl runner = new TestRunnerImpl();

      runner.run( parsedArgs );

   }

   private static Map<String, String> parseMainArgs( String[] args ) {
      if ( ( args == null ) || ( args.length == 0 ) ) {
         return ImmutableMap.of();
      }
      ImmutableMap.Builder<String, String> argBuilder = ImmutableMap.builder();
      for ( String arg : args ) {
         final int splitIndex = arg.indexOf( '=' );
         if ( 0 < splitIndex ) {
            argBuilder.put( arg.substring( 0, splitIndex ), arg.substring( splitIndex + 1 ) );
         }
      }
      return argBuilder.build();
   }
}
