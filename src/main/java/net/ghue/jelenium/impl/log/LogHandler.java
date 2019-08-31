package net.ghue.jelenium.impl.log;

import java.io.Closeable;
import java.io.IOException;
import net.ghue.jelenium.api.log.LogData;

public interface LogHandler extends Closeable {

   LogLevel getLevel();

   void handle( LogData message ) throws IOException;
}
