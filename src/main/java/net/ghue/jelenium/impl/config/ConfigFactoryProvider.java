package net.ghue.jelenium.impl.config;

import java.nio.file.Path;
import java.time.Duration;
import javax.inject.Provider;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Factory;
import org.aeonbits.owner.converters.DurationConverter;
import net.ghue.jelenium.api.log.LogHandlerFactory;
import net.ghue.jelenium.api.suite.TestResultsHandler;
import okhttp3.HttpUrl;

public final class ConfigFactoryProvider implements Provider<Factory> {

   @Override
   public Factory get() {
      final Factory factory = ConfigFactory.newInstance();
      factory.setTypeConverter( Duration.class, DurationConverter.class );
      factory.setTypeConverter( Path.class, PathConverter.class );
      factory.setTypeConverter( LogHandlerFactory.class, LogHandlerFactoryConverter.class );
      factory.setTypeConverter( TestResultsHandler.class, TestResultHandlerConverter.class );
      factory.setTypeConverter( HttpUrl.class, HttpUrlConverter.class );
      return factory;
   }

}
