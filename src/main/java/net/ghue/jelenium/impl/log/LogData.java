package net.ghue.jelenium.impl.log;

import java.time.Duration;
import java.util.Optional;

final class LogData {

   StackTraceElement caller;

   Duration elapsed;

   LogLevel level;

   String message = "";

   Optional<Throwable> throwable = Optional.empty();
}
