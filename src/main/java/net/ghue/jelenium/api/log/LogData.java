package net.ghue.jelenium.api.log;

import java.time.Duration;
import java.util.Optional;
import net.ghue.jelenium.impl.log.LogLevel;

/**
 * TODO make this a real class.
 * 
 * @author Luke Last
 */
public final class LogData {

   public StackTraceElement caller;

   public Duration elapsed;

   public LogLevel level;

   public String message = "";

   public Optional<Throwable> throwable = Optional.empty();
}
