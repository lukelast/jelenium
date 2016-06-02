package net.ghue.jelenium.api;

/**
 * Each test will have it's own {@link TestLog}.
 *
 * @author Luke Last
 */
public interface TestLog {

   /**
    * Debug is the most verbose level.
    *
    * @param message a {@link java.lang.String} object.
    * @param args {@link String#format(String, Object...)} arguments.
    */
   void debug( String message, Object... args );

   /**
    * An error means the test has failed.
    *
    * @param message a {@link java.lang.String} object.
    * @param ex a {@link java.lang.Throwable} object.
    */
   void error( String message, Throwable ex );

   /**
    * Major information events.
    *
    * @param message a {@link java.lang.String} object.
    * @param args a {@link java.lang.Object} object.
    */
   void info( String message, Object... args );

   /**
    * A warning is bad but the test can keep going.
    *
    * @param message a {@link java.lang.String} object.
    * @param args a {@link java.lang.Object} object.
    */
   void warn( String message, Object... args );

}
