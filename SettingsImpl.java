package net.ghue.jelenium.impl;

import static com.google.common.base.Strings.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

/**
 * This implementation must be immutable because instances are shared between test runs.
 * 
 * @author Luke Last
 */
final class SettingsImpl implements JeleniumSettings {

   private final ImmutableMap<String, String> args;

   private final String filter;

   private final Path resultsDir;

   private final Duration retryDelay;

   private final Duration retryTimeout;

   private final HttpUrl url;

   /**
    * Constructor.
    * 
    * @param rawArgs Raw map of all the arguments.
    */
   public SettingsImpl( Map<String, String> rawArgs ) {
      this.args = ImmutableMap.copyOf( rawArgs );
      if ( isNullOrEmpty( args.get( KEY_URL ) ) ) {
         this.url = defaultUrl();
      } else {
         final HttpUrl parsedUrl = HttpUrl.parse( args.get( KEY_URL ) );
         if ( parsedUrl == null ) {
            throw new IllegalArgumentException( "BAD URL: " + args.get( KEY_URL ) );
         } else {
            this.url = parsedUrl;
         }
      }

      final String dir = nullToEmpty( args.get( KEY_RESULTS_DIR ) );
      if ( dir.isEmpty() ) {
         this.resultsDir = DEFAULT_RESULTS_DIR;
      } else {
         this.resultsDir = Paths.get( dir ).toAbsolutePath().normalize();
      }
      this.filter = nullToEmpty( args.get( KEY_FILTER ) ).toLowerCase( Locale.US );
      this.retryTimeout = findDuration( rawArgs, KEY_RETRY_TIMEOUT, DEFAULT_RETRY_TIMEOUT );
      this.retryDelay = findDuration( rawArgs, KEY_RETRY_DELAY, DEFAULT_RETRY_DELAY );
   }

   private HttpUrl defaultUrl() {
      return new HttpUrl.Builder().scheme( "http" ).host( "localhost" ).build();
   }

   private Duration findDuration( Map<String, String> rawArgs, String key, Duration defaultValue ) {
      final String strVal = nullToEmpty( rawArgs.get( key ) );
      if ( !strVal.isEmpty() ) {
         try {
            final Duration dur = Duration.ofMillis( Long.parseLong( strVal ) );
            if ( !dur.isNegative() && !dur.isZero() ) {
               return dur;
            }
         } catch ( NumberFormatException nfe ) {
            nfe.printStackTrace();
         }
      }
      return defaultValue;
   }

   @Override
   public String get( String key ) {
      if ( KEY_URL.equals( key ) ) {
         return this.url.toString();
      }
      return nullToEmpty( this.args.get( key ) );
   }

   @Override
   public String getFilter() {
      return this.filter;
   }

   @Override
   public Path getResultsDir() {
      return this.resultsDir;
   }

   @Override
   public Duration getRetryDelay() {
      return this.retryDelay;
   }

   @Override
   public Duration getRetryTimeout() {
      return this.retryTimeout;
   }

   @Override
   public Optional<HttpUrl> getSecondaryUrl( int index ) {
      return Optional.ofNullable( HttpUrl.parse( get( KEY_URL + index ) ) );
   }

   @Override
   public Optional<Class<JeleniumSuiteRunner>> getSuite() {
      try {
         final String className = get( KEY_TEST_SUITE );
         final Class<?> classInstance = Class.forName( className );
         @SuppressWarnings( "unchecked" )
         final Class<JeleniumSuiteRunner> asType =
               (Class<JeleniumSuiteRunner>) classInstance.asSubclass( JeleniumSuiteRunner.class );
         return Optional.of( asType );
      } catch ( Exception ex ) {
         return Optional.empty();
      }
   }

   @Override
   public HttpUrl getUrl() {
      return this.url;
   }

   @Override
   public boolean is( String key, String expectedValue ) {
      return get( key ).equalsIgnoreCase( nullToEmpty( expectedValue ) );
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append( '\n' ).append( "##### Jelenium Test Arguments: " ).append( '\n' );
      for ( Entry<String, String> arg : this.args.entrySet() ) {
         sb.append( "  " )
           .append( arg.getKey() )
           .append( " = " )
           .append( arg.getValue() )
           .append( '\n' );
      }
      sb.append( '\n' );
      return sb.toString();
   }

}
