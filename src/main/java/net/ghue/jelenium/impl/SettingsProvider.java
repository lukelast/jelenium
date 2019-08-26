package net.ghue.jelenium.impl;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.JeleniumSettings;

class SettingsProvider implements Provider<JeleniumSettings> {

   private final String[] args;

   @Inject
   SettingsProvider( String[] args ) {
      this.args = args;
   }

   @Override
   public JeleniumSettings get() {
      Map<String, String> parsedArgs = parseMainArgs( args );

      //TODO Allow adding of default values.
      return new SettingsImpl( parsedArgs );
   }

   /**
    * Convert the arguments passed on the console to a key-value map. Each argument should be in the
    * format "key=value".
    */
   static Map<String, String> parseMainArgs( String[] args ) {
      if ( args.length == 0 ) {
         return ImmutableMap.of();
      }
      ImmutableMap.Builder<String, String> argBuilder = ImmutableMap.builder();
      for ( String arg : args ) {
         final int splitIndex = arg.indexOf( '=' );
         if ( 0 < splitIndex ) {
            argBuilder.put( arg.substring( 0, splitIndex ), arg.substring( splitIndex + 1 ) );
         }
      }
      return argBuilder.build();
   }

}
