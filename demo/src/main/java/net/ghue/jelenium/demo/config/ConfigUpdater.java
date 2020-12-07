package net.ghue.jelenium.demo.config;

import java.util.Map;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;

/**
 * This is a way to set configuration values instead of using 'jelenium.properties' or
 * 'jelenium.xml' files.
 */
public final class ConfigUpdater implements JeleniumConfigUpdater {

   @Override
   public void override( Map<String, String> config ) {
      // Adding CONFIG values here will override values set anywhere else.
      config.put( "test.key", "override.value" );
   }

   @Override
   public void update( Map<String, String> config ) {
      //config.put( JeleniumConfig.KEY_FILTER, "fail" );

      // Values set here can be overridden by ARGS and ENV variables.
      config.put( "test.key", "update.value" );
   }
}
