package net.ghue.jelenium.impl.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

class LogFormatter {

   static String format( LogData data ) {
      final StringBuilder sb = new StringBuilder();

      //sb.append( '+' );
      final int seconds = (int) data.elapsed.getSeconds();
      if ( seconds < 10 ) {
         sb.append( '0' );
      }
      sb.append( seconds ).append( '.' );

      final int ms = (int) ( data.elapsed.toMillis() % 1_000 );
      if ( ms < 10 ) {
         sb.append( "00" );
      } else if ( ms < 100 ) {
         sb.append( "0" );
      }
      sb.append( ms ).append( "s " );
      sb.append( data.level.toUniformLengthString() ).append( "  " );

      sb.append( data.caller.getClassName() )
        .append( '#' )
        .append( data.caller.getMethodName() )
        .append( '(' )
        .append( data.caller.getLineNumber() )
        .append( ')' );

      sb.append( "\n  " ).append( data.message );
      data.throwable.ifPresent( ex -> {
         if ( ex instanceof InvocationTargetException ) {
            ex = ex.getCause();
         }
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter( sw );
         ex.printStackTrace( pw );
         pw.flush();
         sb.append( "\n  " ).append( sw.toString() );
      } );

      return sb.toString();
   }

}
