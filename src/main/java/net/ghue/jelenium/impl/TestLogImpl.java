package net.ghue.jelenium.impl;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.ghue.jelenium.api.TestLog;

/**
 * Implement {@link TestLog} by printing to standard out and standard error.
 * 
 * @author Luke Last
 */
final class TestLogImpl implements TestLog {

   public TestLogImpl( Path testResultsDir ) {

   }

   @Override
   public void debug( String message, Object... args ) {
      System.out.println( String.format( message, args ) );
   }

   @Override
   public void error( String message, @Nullable Throwable ex ) {
      System.err.println( "ERROR: " + message );

      if ( ex instanceof InvocationTargetException ) {
         ex = ex.getCause();
      }
      if ( ex != null ) {
         ex.printStackTrace();
      }
   }

   @Override
   public void info( String message, Object... args ) {
      System.out.println( String.format( message, args ) );
   }

   @Override
   public void warn( String message, Object... args ) {
      System.err.println( String.format( message, args ) );
   }

}
