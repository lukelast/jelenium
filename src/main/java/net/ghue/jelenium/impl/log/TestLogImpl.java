package net.ghue.jelenium.impl.log;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.log.LogApi;
import net.ghue.jelenium.api.log.TestLog;

/**
 * Implement {@link TestLog}
 * 
 * @author Luke Last
 */
public final class TestLogImpl implements TestLog, Closeable {

   private final List<LogHandler> handlers;

   private LogApiImpl lastLogger;

   private final Instant startTime;

   public TestLogImpl( Instant startTime, Collection<LogHandler> handlers ) {
      this.handlers = ImmutableList.copyOf( handlers );
      this.startTime = startTime;
   }

   /**
    * Closes handlers. Mainly used to close files.
    */
   @Override
   public void close() throws IOException {
      for ( LogHandler handler : this.handlers ) {
         handler.close();
      }
   }

   private LogApi createLogger( LogLevel level ) {
      if ( this.lastLogger != null && !this.lastLogger.isLogged() ) {
         this.lastLogger.log();
      }

      final LogApiImpl logger = new LogApiImpl( level, handlers, startTime );
      this.lastLogger = logger;
      return logger;
   }

   @Override
   public LogApi debug() {
      return createLogger( LogLevel.DEBUG );
   }

   @Override
   public LogApi error() {
      return createLogger( LogLevel.ERROR );
   }

   @Override
   public LogApi info() {
      return createLogger( LogLevel.INFO );
   }

   @Override
   public LogApi warn() {
      return createLogger( LogLevel.WARN );
   }

}
