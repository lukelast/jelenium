package net.ghue.jelenium.impl.log;

public enum LogLevel {

   DEBUG {

      @Override
      public boolean atOrAbove( LogLevel level ) {
         return true;
      }
   },

   ERROR {

      @Override
      public boolean atOrAbove( LogLevel level ) {
         return level == ERROR;
      }
   },

   INFO {

      @Override
      public boolean atOrAbove( LogLevel level ) {
         return level != DEBUG;
      }
   },

   WARN {

      @Override
      public boolean atOrAbove( LogLevel level ) {
         switch ( level ) {
         case DEBUG:
         case INFO:
            return false;
         case ERROR:
         case WARN:
            return true;
         }
         throw new IllegalStateException( "unexpected" );
      }
   };

   public abstract boolean atOrAbove( LogLevel level );

   public String toUniformLengthString() {
      switch ( this ) {
      case DEBUG:
         return "DEBUG";
      case ERROR:
         return "ERROR";
      case INFO:
         return "INFO ";
      case WARN:
         return "WARN ";
      }
      throw new IllegalStateException();
   }

}
