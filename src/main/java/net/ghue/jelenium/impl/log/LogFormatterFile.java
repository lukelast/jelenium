package net.ghue.jelenium.impl.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import net.ghue.jelenium.api.log.LogData;
import net.ghue.jelenium.api.log.LogFormatter;

public final class LogFormatterFile implements LogFormatter {

   @Override
   public String format( LogData data ) {
      final StringBuilder sb = new StringBuilder();

      // Log level.
      sb.append( "    " ).append( data.level.toUniformLengthString() ).append( " " );

      // Elapsed time from test start.
      sb.append( "+" );
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
      sb.append( ms ).append( "s  " );

      // Log site.
      sb.append( data.caller.getClassName() )
        .append( '#' )
        .append( data.caller.getMethodName() )
        .append( '(' )
        .append( data.caller.getLineNumber() )
        .append( ')' );

      if ( !data.message.isEmpty() ) {
         sb.append( "\n" ).append( data.message );
      }
      data.throwable.ifPresent( ex -> {
         if ( ex instanceof InvocationTargetException ) {
            ex = ex.getCause();
         }
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter( sw );
         ex.printStackTrace( pw );
         pw.flush();
         sb.append( "\n\n" ).append( sw.toString().replace( "\t", "  " ) );
      } );

      if ( data.level != LogLevel.DEBUG ) {
         sb.append( '\n' );
      }

      return sb.toString();
   }

}
