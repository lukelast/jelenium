package net.ghue.jelenium;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.impl.TestRunnerImpl;

/**
 * <p>
 * A test runner main class that can be used as an entry point to run selenium tests.
 * </p>
 *
 * @author Luke Last
 */
public final class Runner {

   /**
    * <p>
    * Standard java main method.
    * </p>
    *
    * @param args Command line arguments.
    * @throws java.lang.Exception if any.
    */
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

   /**
    * Convert the arguments passed on the console to a key-value map. Each argument should be in the
    * format "key=value".
    */
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