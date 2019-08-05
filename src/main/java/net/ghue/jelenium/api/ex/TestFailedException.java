package net.ghue.jelenium.api.ex;

public final class TestFailedException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public TestFailedException( String message ) {
      super( message );
   }

}
