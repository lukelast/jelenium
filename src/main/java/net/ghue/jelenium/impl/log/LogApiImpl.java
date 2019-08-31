package net.ghue.jelenium.impl.log;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.ghue.jelenium.api.log.LogApi;
import net.ghue.jelenium.api.log.LogData;

final class LogApiImpl implements LogApi {

   final StackTraceElement caller;

   private final List<LogHandler> handlers;

   private final LogLevel level;

   private boolean logged = false;

   private StringBuilder messages = new StringBuilder();

   private final Instant messageTime = Instant.now();

   private final Instant startTime;

   private Optional<Throwable> throwable = Optional.empty();

   LogApiImpl( LogLevel level, List<LogHandler> handlers, Instant startTime ) {
      this.level = level;
      this.handlers = handlers;
      this.startTime = startTime;
      // TODO Replace with stack walker after upgrading java version.
      this.caller = Thread.currentThread().getStackTrace()[4];
   }

   @Override
   public LogApi ex( @Nullable Throwable exception ) {
      this.throwable = Optional.ofNullable( exception );
      return this;
   }

   boolean isLogged() {
      return this.logged;
   }

   @Override
   public void log() {
      this.logged = true;
      final LogData data = new LogData();

      data.level = this.level;
      data.message = this.messages.toString();
      data.elapsed = Duration.between( startTime, messageTime );
      data.caller = this.caller;
      // TODO Replace with stack walker after upgrading java version.
      //data.caller = Thread.currentThread().getStackTrace()[2];
      data.throwable = this.throwable;

      for ( LogHandler handler : this.handlers ) {
         if ( handler.getLevel().atOrAbove( this.level ) ) {
            try {
               handler.handle( data );
            } catch ( IOException ex ) {
               throw new RuntimeException( ex );
            }
         }
      }
   }

   @Override
   public LogApi msg( String message ) {
      this.messages.append( message );
      return this;
   }

   @Override
   public LogApi msg( String message, Object... formatArgs ) {
      return msg( String.format( message, formatArgs ) );
   }

   @Override
   public LogApi newline() {
      return msg( "\n" );
   }

}
