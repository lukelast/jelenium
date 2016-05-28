package net.ghue.jelenium.api;

/**
 * <p>
 * TestLog interface.
 * </p>
 *
 * @author Luke Last
 */
public interface TestLog {

   /**
    * <p>
    * debug.
    * </p>
    *
    * @param message a {@link java.lang.String} object.
    * @param args a {@link java.lang.Object} object.
    */
   void debug( String message, Object... args );

   /**
    * <p>
    * error.
    * </p>
    *
    * @param message a {@link java.lang.String} object.
    * @param ex a {@link java.lang.Throwable} object.
    */
   void error( String message, Throwable ex );

   /**
    * <p>
    * info.
    * </p>
    *
    * @param message a {@link java.lang.String} object.
    * @param args a {@link java.lang.Object} object.
    */
   void info( String message, Object... args );

   /**
    * <p>
    * warn.
    * </p>
    *
    * @param message a {@link java.lang.String} object.
    * @param args a {@link java.lang.Object} object.
    */
   void warn( String message, Object... args );

}
