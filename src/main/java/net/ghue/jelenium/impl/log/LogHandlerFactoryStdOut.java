package net.ghue.jelenium.impl.log;

import java.nio.file.Path;
import java.time.Instant;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.log.LogHandlerFactory;

public class LogHandlerFactoryStdOut implements LogHandlerFactory {

   @Override
   public LogHandler create( TestName name, Instant startTime, Path testResultsDir ) {
      return new LogHandlerStdOut( LogLevel.DEBUG, new LogFormatterFile() );
   }

}
