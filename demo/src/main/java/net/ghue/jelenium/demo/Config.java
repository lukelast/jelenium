package net.ghue.jelenium.demo;

import java.util.Map;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;

public final class Config implements JeleniumConfigUpdater {

   @Override
   public void override( Map<String, String> config ) {}

   @Override
   public void update( Map<String, String> config ) {
      //config.put( JeleniumConfig.KEY_FILTER, "fail" );
   }

}
