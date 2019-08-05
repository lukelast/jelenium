package net.ghue.jelenium.api.ex;

public final class TestErrorException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public TestErrorException( String message ) {
      super( message );
   }

}
