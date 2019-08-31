package net.ghue.jelenium.impl.config;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.annotation.Nullable;
import org.aeonbits.owner.Converter;

public final class PathConverter implements Converter<Path> {

   @Override
   public Path convert( @Nullable Method method, @Nullable String input ) {
      Objects.requireNonNull( method );
      Objects.requireNonNull( input );
      return Paths.get( input ).normalize().toAbsolutePath();
   }

}
