package net.ghue.jelenium.impl.log;

import java.nio.file.Path;
import java.time.Instant;
import net.ghue.jelenium.api.log.LogHandlerFactory;
import net.ghue.jelenium.api.test.TestName;

public class LogHandlerFactoryFileWarn implements LogHandlerFactory {

   @Override
   public LogHandler create( TestName name, Instant startTime, Path testResultsDir ) {
      return new LogHandlerFile( LogLevel.WARN,
                                 new LogFormatterFile(),
                                 name,
                                 startTime,
                                 testResultsDir );
   }
}
