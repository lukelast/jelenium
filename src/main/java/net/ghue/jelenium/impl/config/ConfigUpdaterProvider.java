package net.ghue.jelenium.impl.config;

import java.util.Collection;
import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Provider;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;
import net.ghue.jelenium.impl.Utils;
import net.ghue.jelenium.impl.test.Scanner;

public final class ConfigUpdaterProvider implements Provider<Collection<JeleniumConfigUpdater>> {

   private final Scanner scanner;

   @Inject
   ConfigUpdaterProvider( Scanner scanner ) {
      this.scanner = scanner;
   }

   @Override
   public Collection<JeleniumConfigUpdater> get() {
      // Maybe want to support priority based on annotations.

      return scanner.findConfigUpdaters()
                    .sorted( Comparator.comparing( Class::getCanonicalName ) )
                    .map( Utils::newInstance )
                    .collect( ImmutableList.toImmutableList() );
   }

}
