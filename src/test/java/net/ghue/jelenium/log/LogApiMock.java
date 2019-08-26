package net.ghue.jelenium.log;

import net.ghue.jelenium.api.log.LogApi;

public final class LogApiMock implements LogApi {

   @Override
   public LogApi ex( Throwable throwable ) {
      return this;
   }

   @Override
   public void log() {}

   @Override
   public LogApi msg( String message ) {
      return this;
   }

   @Override
   public LogApi msg( String message, Object... formatArgs ) {
      return this;
   }

}
