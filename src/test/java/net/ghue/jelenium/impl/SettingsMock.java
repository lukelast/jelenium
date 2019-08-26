package net.ghue.jelenium.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

public class SettingsMock implements JeleniumSettings {

   @Override
   public String get( String key ) {
      return "";
   }

   @Override
   public String getFilter() {
      return "";
   }

   @Override
   public Path getResultsDir() {
      // TODO Auto-generated method stub
      return Paths.get( "" );
   }

   @Override
   public Duration getRetryDelay() {
      return Duration.ZERO;
   }

   @Override
   public Duration getRetryTimeout() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Optional<HttpUrl> getSecondaryUrl( int number ) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Optional<Class<JeleniumSuiteRunner>> getSuite() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public HttpUrl getUrl() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean is( String key, String expectedValue ) {
      return false;
   }

}
