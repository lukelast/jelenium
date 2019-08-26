package net.ghue.jelenium.impl.log;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class LogHandlerFile extends LogHandlerBase {

   final Path file;

   final Writer writer;

   public LogHandlerFile( LogLevel level, Path file ) {
      super( level );
      this.file = file;
      try {
         Files.createDirectories( file.getParent() );
         this.writer = Files.newBufferedWriter( file,
                                                StandardOpenOption.CREATE_NEW,
                                                StandardOpenOption.TRUNCATE_EXISTING );
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   @Override
   public void close() throws IOException {
      this.writer.close();
   }

   @Override
   public void handle( LogData data ) throws IOException {
      this.writer.append( LogFormatter.format( data ) ).append( '\n' );
   }

}
