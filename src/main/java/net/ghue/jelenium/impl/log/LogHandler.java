package net.ghue.jelenium.impl.log;

import java.io.Closeable;
import java.io.IOException;

public interface LogHandler extends Closeable {

   LogLevel getLevel();

   void handle( LogData message ) throws IOException;
}
