package net.ghue.jelenium.impl.log;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Locale;
import javax.annotation.concurrent.NotThreadSafe;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.log.LogData;
import net.ghue.jelenium.api.log.LogFormatter;

@NotThreadSafe
public final class LogHandlerFile extends LogHandlerBase {

   private final Path file;

   private Writer writer;

   public LogHandlerFile( LogLevel level, LogFormatter formatter, TestName name, Instant startTime,
                          Path testResultsDir ) {
      super( level, formatter );
      this.file = testResultsDir.resolve( name.getShortName() +
                                          "-" +
                                          level.toString().toLowerCase( Locale.ENGLISH ) +
                                          ".log" );
   }

   @Override
   public void close() throws IOException {
      if ( this.writer != null ) {
         this.writer.close();
      }
   }

   private Writer getOrCreateWriter() {
      // These handlers should only be used from 1 thread.
      if ( this.writer == null ) {
         try {
            Files.createDirectories( file.getParent() );
            this.writer = Files.newBufferedWriter( file,
                                                   StandardOpenOption.CREATE_NEW,
                                                   StandardOpenOption.TRUNCATE_EXISTING );
         } catch ( IOException ex ) {
            throw new RuntimeException( ex );
         }
      }
      return this.writer;
   }

   @Override
   public void handle( LogData data ) throws IOException {
      getOrCreateWriter().append( this.formatter.format( data ) ).append( '\n' );
   }

}
