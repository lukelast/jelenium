package net.ghue.jelenium.api.ex;

/**
 * Throw this exception to mark the test as skipped. This can be used to conditionally skip a test.
 */
public final class SkipTestException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public SkipTestException() {}

   public SkipTestException( String message ) {
      super( message );
   }

   public SkipTestException( String message, Throwable cause ) {
      super( message, cause );
   }

   public SkipTestException( String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace ) {
      super( message, cause, enableSuppression, writableStackTrace );
   }

   public SkipTestException( Throwable cause ) {
      super( cause );
   }

}
