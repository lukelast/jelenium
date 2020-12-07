package net.ghue.jelenium.impl.config;

import java.util.Collection;
import javax.inject.Provider;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;
import net.ghue.jelenium.impl.Scanner;

public final class ConfigUpdaterProvider implements Provider<Collection<JeleniumConfigUpdater>> {

   @Override
   public Collection<JeleniumConfigUpdater> get() {

      //TODO support ordering.

      return Scanner.findConfigUpdaters().stream().map( cl -> {
         try {
            return cl.newInstance();
         } catch ( InstantiationException | IllegalAccessException ex ) {
            throw new RuntimeException( ex );
         }
      } ).collect( ImmutableList.toImmutableList() );

   }

}
