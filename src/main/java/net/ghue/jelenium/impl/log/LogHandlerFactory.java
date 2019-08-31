package net.ghue.jelenium.impl.log;

import java.nio.file.Path;
import java.time.Instant;
import net.ghue.jelenium.api.TestName;

public interface LogHandlerFactory {

   LogHandler create( TestName name, Instant startTime, Path testResultsDir );

}
