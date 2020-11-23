package net.ghue.jelenium.impl.config;

import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aeonbits.owner.Converter;
import net.ghue.jelenium.api.log.LogHandlerFactory;

public final class LogHandlerFactoryConverter implements Converter<LogHandlerFactory> {

   @Override
   public LogHandlerFactory convert( @Nullable Method method, @Nullable String input ) {
      try {
         return Class.forName( input ).asSubclass( LogHandlerFactory.class ).newInstance();
      } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException ex ) {
         throw new RuntimeException( "'" +
                                     input +
                                     "' must be an instance of : " +
                                     LogHandlerFactory.class.getCanonicalName(),
                                     ex );
      }
   }
}
