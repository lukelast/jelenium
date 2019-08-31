package net.ghue.jelenium.impl.log;

import net.ghue.jelenium.api.log.LogData;
import net.ghue.jelenium.api.log.LogFormatter;

public final class LogHandlerStdOut extends LogHandlerBase {

   public LogHandlerStdOut( LogLevel level, LogFormatter formatter ) {
      super( level, formatter );
   }

   @Override
   public void handle( LogData data ) {
      System.out.println( this.formatter.format( data ) );
   }
}
