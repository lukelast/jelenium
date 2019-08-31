package net.ghue.jelenium.impl.config;

import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aeonbits.owner.Converter;
import net.ghue.jelenium.api.TestResultsHandler;

public final class TestResultHandlerConverter implements Converter<TestResultsHandler> {

   @Override
   public TestResultsHandler convert( @Nullable Method method, @Nullable String input ) {
      try {
         return Class.forName( input ).asSubclass( TestResultsHandler.class ).newInstance();
      } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException ex ) {
         throw new RuntimeException( ex );
      }
   }
}
