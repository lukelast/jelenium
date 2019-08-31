package net.ghue.jelenium.api.log;

public interface LogApi {

   /**
    * Add an exception to the log message.
    * 
    * @param throwable
    * @return self.
    */
   LogApi ex( Throwable throwable );

   void log();

   LogApi msg( String message );

   LogApi msg( String message, Object... formatArgs );

   LogApi newline();
}
