package net.ghue.jelenium.api.config;

import java.util.Map;

public interface JeleniumConfigUpdater {

   void override( Map<String, String> config );

   void update( Map<String, String> config );
}
