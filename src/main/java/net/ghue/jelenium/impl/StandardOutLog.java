package net.ghue.jelenium.impl;

import java.lang.reflect.InvocationTargetException;
import net.ghue.jelenium.api.TestLog;

/**
 * Implement {@link TestLog} by printing to standard out and standard error.
 * 
 * @author Luke Last
 */
final class StandardOutLog implements TestLog {

   @Override
   public void debug( String message, Object... args ) {
      System.out.println( String.format( message, args ) );
   }

   @Override
   public void error( String message, Throwable ex ) {
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
