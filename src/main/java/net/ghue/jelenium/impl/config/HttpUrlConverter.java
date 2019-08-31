package net.ghue.jelenium.impl.config;

import java.lang.reflect.Method;
import java.util.Objects;
import javax.annotation.Nullable;
import org.aeonbits.owner.Converter;
import okhttp3.HttpUrl;

public final class HttpUrlConverter implements Converter<HttpUrl> {

   @Override
   public HttpUrl convert( @Nullable Method method, @Nullable String input ) {
      return HttpUrl.parse( Objects.requireNonNull( input ) );
   }
}
