package net.ghue.jelenium.impl.config;

import static com.google.common.base.Strings.nullToEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Provider;
import org.aeonbits.owner.Factory;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;

public final class ConfigProvider implements Provider<JeleniumConfig> {

   private static final String ENV_VAR_PREFIX = "jelenium.";

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

   private final String[] args;

   private final Provider<Factory> factoryProvider;

   private final Provider<Collection<JeleniumConfigUpdater>> settingsUpdaterProvider;

   @Inject
   ConfigProvider( String[] args,
                   Provider<Collection<JeleniumConfigUpdater>> settingsUpdaterProvider,
                   Provider<Factory> factoryProvider ) {
      this.args = args;
      this.settingsUpdaterProvider = settingsUpdaterProvider;
      this.factoryProvider = factoryProvider;
   }

   public Map<String, String> filterEnvVarPrefixes( Map<?, ?> env ) {
      final Map<String, String> rawArgs = new HashMap<>();
      for ( Entry<?, ?> entry : env.entrySet() ) {
         final String key = entry.getKey().toString();
         final String value = nullToEmpty( entry.getValue().toString() );
         if ( key.startsWith( ENV_VAR_PREFIX ) && !value.isEmpty() ) {
            final String trimmedKey = key.substring( ENV_VAR_PREFIX.length() );
            rawArgs.put( trimmedKey, value );
         }
      }
      return rawArgs;
   }

   @Override
   public JeleniumConfig get() {
      final Collection<JeleniumConfigUpdater> updaters = settingsUpdaterProvider.get();

      final List<Map<?, ?>> imports = new ArrayList<>();

      for ( JeleniumConfigUpdater updater : updaters ) {
         Map<String, String> configArgs = new HashMap<>();
         updater.override( configArgs );
         imports.add( configArgs );
      }

      final Map<String, String> mainArgs = parseMainArgs( args );
      imports.add( mainArgs );
      imports.add( filterEnvVarPrefixes( System.getenv() ) );
      imports.add( filterEnvVarPrefixes( System.getProperties() ) );

      for ( JeleniumConfigUpdater updater : updaters ) {
         Map<String, String> configArgs = new HashMap<>();
         updater.update( configArgs );
         imports.add( configArgs );
      }

      JeleniumConfig cfg = this.factoryProvider.get()
                                               .create( JeleniumConfig.class,
                                                        imports.toArray( new Map<?, ?>[0] ) );
      return cfg;
   }

}
