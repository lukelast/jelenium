package net.ghue.jelenium.api.config;

import java.util.Map;

/**
 * Create an implement of this interface to set configuration values in code.
 * 
 * @author Luke Last
 */
public interface JeleniumConfigUpdater {

   /**
    * Values added here will take precedence over all others.
    * 
    * @param config
    */
   void override( Map<String, String> config );

   /**
    * Normal place to add configuration values.
    * 
    * @param config
    */
   void update( Map<String, String> config );
}
