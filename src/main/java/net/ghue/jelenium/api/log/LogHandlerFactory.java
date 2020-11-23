package net.ghue.jelenium.api.log;

import java.nio.file.Path;
import java.time.Instant;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.impl.log.LogHandler;

/**
 * Creates a {@link LogHandler}. Implementations must have a public no-arg constructor.
 * 
 * @author Luke Last
 */
public interface LogHandlerFactory {

   /**
    * @param name The name of the test this log handler is for.
    * @param startTime The start time for the test.
    * @param testResultsDir The directory where results are to be stored for this test.
    * @return A new {@link LogHandler} that will do something with logging events.
    */
   LogHandler create( TestName name, Instant startTime, Path testResultsDir );
}
