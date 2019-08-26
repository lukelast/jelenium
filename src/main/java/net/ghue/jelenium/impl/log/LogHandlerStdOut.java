package net.ghue.jelenium.impl.log;

public final class LogHandlerStdOut extends LogHandlerBase {

   public LogHandlerStdOut( LogLevel level ) {
      super( level );
   }

   @Override
   public void handle( LogData data ) {
      System.out.println( LogFormatter.format( data ) );
   }
}
