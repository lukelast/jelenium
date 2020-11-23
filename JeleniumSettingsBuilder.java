package net.ghue.jelenium.api.settings;

import static com.google.common.base.Strings.nullToEmpty;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import org.assertj.core.util.Files;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.log.LogHandlerFactory;
import net.ghue.jelenium.impl.log.LogHandlerFactoryFileDebug;
import net.ghue.jelenium.impl.log.LogHandlerFactoryFileWarn;
import net.ghue.jelenium.impl.log.LogHandlerFactoryStdOut;

public final class JeleniumSettingsBuilder {

   private static final String ENV_VAR_PREFIX = "jelenium.";

   /**
    * We want the keys to be in order for when printing.
    */
   private Map<String, String> args = new TreeMap<>();

   private String filter = "";

   private List<LogHandlerFactory> logHandlers = new ArrayList<>();

   private Path resultsDir = Paths.get( "" );

   private Duration retryDelay = Duration.ZERO;

   private Duration retryTimeout = Duration.ZERO;

   private Optional<Class<JeleniumSuiteRunner>> suite = Optional.empty();

   private int testRetries = -1;

   private HttpUrl url;

   public JeleniumSettingsBuilder applyDefaults() {
      this.args.clear();

      this.logHandlers.clear();
      this.logHandlers.add( new LogHandlerFactoryStdOut() );
      this.logHandlers.add( new LogHandlerFactoryFileDebug() );
      this.logHandlers.add( new LogHandlerFactoryFileWarn() );

      this.filter = "";
      this.resultsDir = JeleniumSettings.DEFAULT_RESULTS_DIR;
      this.retryDelay = JeleniumSettings.DEFAULT_RETRY_DELAY;
      this.retryTimeout = JeleniumSettings.DEFAULT_RETRY_TIMEOUT;
      this.url = new HttpUrl.Builder().scheme( "http" ).host( "localhost" ).build();
      this.testRetries = JeleniumSettings.DEFAULT_TEST_RETRIES;
      this.suite = Optional.empty();

      return this;
   }

   public JeleniumSettingsBuilder applyMockValues() {
      this.applyDefaults();
      this.args.clear();

      this.logHandlers.clear();

      this.resultsDir = Files.temporaryFolder().toPath();

      return this;
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

   private int findInt( String key, int defaultValue ) {
      try {
         return Integer.parseInt( this.args.get( key ) );
      } catch ( Exception ex ) {
         return defaultValue;
      }
   }

   public Map<String, String> getArgs() {
      return args;
   }

   public String getFilter() {
      return filter;
   }

   public List<LogHandlerFactory> getLogHandlers() {
      return logHandlers;
   }

   public Path getResultsDir() {
      return resultsDir;
   }

   public Duration getRetryDelay() {
      return retryDelay;
   }

   public Duration getRetryTimeout() {
      return retryTimeout;
   }

   public Optional<Class<JeleniumSuiteRunner>> getSuite() {
      return suite;
   }

   public int getTestRetries() {
      return testRetries;
   }

   public HttpUrl getUrl() {
      return url;
   }

   public JeleniumSettingsBuilder parseArgs( Map<String, String> rawArgs ) {
      this.args.putAll( rawArgs );

      final String rawUrl = args.getOrDefault( JeleniumSettings.KEY_URL, "" );
      if ( !rawUrl.isEmpty() ) {
         final HttpUrl parsedUrl = HttpUrl.parse( args.get( JeleniumSettings.KEY_URL ) );
         if ( parsedUrl == null ) {
            throw new IllegalArgumentException( "BAD URL: " +
                                                args.get( JeleniumSettings.KEY_URL ) );
         } else {
            this.url = parsedUrl;
            // Write back to the raw ARGS in case the parsing normalized anything.
            this.args.put( JeleniumSettings.KEY_URL, this.url.toString() );
         }
      }

      final String dir = nullToEmpty( args.get( JeleniumSettings.KEY_RESULTS_DIR ) );
      if ( !dir.isEmpty() ) {
         this.resultsDir = Paths.get( dir ).toAbsolutePath().normalize();
      }
      final String argFilter =
            nullToEmpty( args.get( JeleniumSettings.KEY_FILTER ) ).toLowerCase( Locale.US );
      if ( !argFilter.isEmpty() ) {
         this.filter = argFilter;
      }

      this.retryTimeout =
            findDuration( rawArgs, JeleniumSettings.KEY_RETRY_TIMEOUT, this.retryTimeout );
      this.retryDelay = findDuration( rawArgs, JeleniumSettings.KEY_RETRY_DELAY, this.retryDelay );
      this.testRetries = this.findInt( JeleniumSettings.KEY_TEST_RETRIES, this.testRetries );

      try {
         final String className = nullToEmpty( args.get( JeleniumSettings.KEY_TEST_SUITE ) );
         if ( !className.isEmpty() ) {
            final Class<?> classInstance = Class.forName( className );
            @SuppressWarnings( "unchecked" )
            final Class<JeleniumSuiteRunner> asType =
                  (Class<JeleniumSuiteRunner>) classInstance.asSubclass( JeleniumSuiteRunner.class );
            this.suite = Optional.of( asType );
         }
      } catch ( Exception ex ) {
         ex.printStackTrace();
      }

      //TODO this.logHandlers = this.logHandlers;

      return this;
   }

   /**
    * Parse settings from environment variables. All keys should be prefixed with
    * {@value #ENV_VAR_PREFIX}.
    * 
    * @return Self.
    */
   public JeleniumSettingsBuilder parseEnvVars() {
      return parseEnvVars( System.getenv() );
   }

   /**
    * This method is visible for unit testing. Usually you should be using {@link #parseEnvVars()}.
    * 
    * @param env {@link System#getenv()}
    * @return Self.
    * @see #parseEnvVars()
    */
   public JeleniumSettingsBuilder parseEnvVars( Map<String, String> env ) {
      final Map<String, String> rawArgs = new HashMap<>();
      for ( Entry<String, String> entry : env.entrySet() ) {
         final String key = entry.getKey();
         final String value = nullToEmpty( entry.getValue() );
         if ( key.startsWith( ENV_VAR_PREFIX ) && !value.isEmpty() ) {
            final String trimmedKey = key.substring( ENV_VAR_PREFIX.length() );
            rawArgs.put( trimmedKey, entry.getValue() );
         }
      }
      return parseArgs( rawArgs );
   }

   public JeleniumSettingsBuilder parseSystemProps() {
      final Map<String, String> systemArgs =
            System.getProperties()
                  .entrySet()
                  .stream()
                  .collect( ImmutableMap.toImmutableMap( entry -> entry.getKey().toString(),
                                                         entry -> entry.getValue().toString() ) );
      return parseEnvVars( systemArgs );
   }

   public JeleniumSettingsBuilder withArgs( Map<String, String> rawArgs ) {
      this.args = rawArgs;
      return this;
   }

   public JeleniumSettingsBuilder withFilter( String filterString ) {
      this.filter = filterString;
      return this;
   }

   public JeleniumSettingsBuilder withLogHandlers( List<LogHandlerFactory> handlers ) {
      this.logHandlers = handlers;
      return this;
   }

   public JeleniumSettingsBuilder withResultsDir( Path path ) {
      this.resultsDir = path;
      return this;
   }

   public JeleniumSettingsBuilder withRetryDelay( Duration duration ) {
      this.retryDelay = duration;
      return this;
   }

   public JeleniumSettingsBuilder withRetryTimeout( Duration duration ) {
      this.retryTimeout = duration;
      return this;
   }

   public JeleniumSettingsBuilder withSuite( Class<JeleniumSuiteRunner> jeleniumSuiteRunner ) {
      this.suite = Optional.ofNullable( jeleniumSuiteRunner );
      return this;
   }

   public JeleniumSettingsBuilder withTestRetries( int retries ) {
      this.testRetries = retries;
      return this;
   }

   public JeleniumSettingsBuilder withUrl( HttpUrl httpUrl ) {
      this.url = httpUrl;
      return this;
   }
}
