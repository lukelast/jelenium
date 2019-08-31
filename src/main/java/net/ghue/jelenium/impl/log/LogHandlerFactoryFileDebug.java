package net.ghue.jelenium.impl.log;

import java.nio.file.Path;
import java.time.Instant;
import net.ghue.jelenium.api.TestName;

public class LogHandlerFactoryFileDebug implements LogHandlerFactory {

   @Override
   public LogHandler create( TestName name, Instant startTime, Path testResultsDir ) {
      return new LogHandlerFile( LogLevel.DEBUG,
                                 new LogFormatterFile(),
                                 name,
                                 startTime,
                                 testResultsDir );
   }
}
