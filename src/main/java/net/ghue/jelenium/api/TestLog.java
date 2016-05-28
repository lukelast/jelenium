package net.ghue.jelenium.api;

public interface TestLog {

   void debug( String message, Object... args );

   void error( String message, Throwable ex );

   void info( String message, Object... args );

   void warn( String message, Object... args );

}
