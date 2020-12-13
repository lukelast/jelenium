package net.ghue.jelenium.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface Utils {

   /**
    * Only seconds resolution.
    * 
    * @param time
    * @return Date and time in string form.
    */
   static String formatDateForFilename( LocalDateTime time ) {
      return time.format( DateTimeFormatter.ofPattern( "uuMMdd'@'HH-mm-ss", Locale.US ) );
   }

   /**
    * With milliseconds.
    * 
    * @param time
    * @return Date and time in string form.
    */
   static String formatDateForFilenameMs( LocalDateTime time ) {
      return time.format( DateTimeFormatter.ofPattern( "uuMMdd'@'HH-mm-ss-SSS", Locale.US ) );
   }

   static <T> T newInstance( Class<T> clazz ) {
      try {
         return clazz.newInstance();
      } catch ( InstantiationException | IllegalAccessException ex ) {
         throw new RuntimeException( ex );
      }
   }

   static void sleep( double seconds ) {
      try {
         Thread.sleep( (long) ( seconds * 1_000 ) );
      } catch ( InterruptedException ex ) {
         throw new RuntimeException( ex );
      }
   }

}
