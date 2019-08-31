package net.ghue.jelenium.impl.log;

import java.io.IOException;
import net.ghue.jelenium.api.log.LogFormatter;

abstract class LogHandlerBase implements LogHandler {

   final LogFormatter formatter;

   final LogLevel level;

   LogHandlerBase( LogLevel level, LogFormatter formatter ) {
      this.level = level;
      this.formatter = formatter;
   }

   @Override
   public void close() throws IOException {}

   @Override
   public LogLevel getLevel() {
      return this.level;
   }

}
