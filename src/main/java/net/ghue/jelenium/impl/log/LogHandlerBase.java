package net.ghue.jelenium.impl.log;

import java.io.IOException;

abstract class LogHandlerBase implements LogHandler {

   final LogLevel level;

   LogHandlerBase( LogLevel level ) {
      this.level = level;
   }

   @Override
   public void close() throws IOException {}

   @Override
   public LogLevel getLevel() {
      return this.level;
   }

}
