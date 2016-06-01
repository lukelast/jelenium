package net.ghue.jelenium.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;

/**
 * This implementation must be immutable because instances are shared between test runs.
 * 
 * @author Luke Last
 */
final class SettingsImpl implements JeleniumSettings {

   private final ImmutableMap<String, String> args;

   private final HttpUrl url;

   private final Path resultsDir;

   /**
    * Constructor for TestArgsImpl.
    * 
    * @param rawArgs Raw map of all the arguments.
    */
   public SettingsImpl( Map<String, String> rawArgs ) {
      this.args = ImmutableMap.copyOf( rawArgs );
      if ( Strings.isNullOrEmpty( args.get( KEY_URL ) ) ) {
         this.url = defaultUrl();
      } else {
         this.url = HttpUrl.parse( args.get( KEY_URL ) );
         if ( this.url == null ) {
            throw new IllegalArgumentException( "BAD URL: " + args.get( KEY_URL ) );
         }
      }

      final String dir = Strings.nullToEmpty( args.get( KEY_RESULTS_DIR ) );
      if ( dir.isEmpty() ) {
         this.resultsDir = DEFAULT_RESULTS_DIR;
      } else {
         this.resultsDir = Paths.get( dir ).toAbsolutePath().normalize();
      }
   }

   private HttpUrl defaultUrl() {
      return new HttpUrl.Builder().scheme( "http" ).host( "localhost" ).build();
   }

   @Override
   public String get( String key ) {
      if ( KEY_URL.equals( key ) ) {
         return this.url.toString();
      }
      return Strings.nullToEmpty( this.args.get( key ) );
   }

   @Override
   public Optional<HttpUrl> getSecondaryUrl( int index ) {
      return Optional.ofNullable( HttpUrl.parse( get( KEY_URL + index ) ) );
   }

   @Override
   public HttpUrl getUrl() {
      return this.url;
   }

   @Override
   public Path getResultsDir() {
      return this.resultsDir;
   }

}
