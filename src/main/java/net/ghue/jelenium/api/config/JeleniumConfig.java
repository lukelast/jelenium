package net.ghue.jelenium.api.config;

import static com.google.common.base.Strings.nullToEmpty;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.DisableFeature;
import org.aeonbits.owner.Config.DisableableFeature;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import net.ghue.jelenium.api.log.LogHandlerFactory;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.suite.TestResultsHandler;
import net.ghue.jelenium.api.suite.WebDriverProvider;
import okhttp3.HttpUrl;

/**
 * 'jelenium-priority' files can be created as a way to override values without modifying the main
 * CONFIG file.
 */
@LoadPolicy( LoadType.MERGE )
@DisableFeature( {
      DisableableFeature.VARIABLE_EXPANSION, DisableableFeature.PARAMETER_FORMATTING } )
@Sources( {
      "classpath:jelenium-priority.xml", "classpath:jelenium-priority.properties",
      "classpath:jelenium.xml", "classpath:jelenium.properties" } )
public interface JeleniumConfig extends Accessible {

   String KEY_FILTER = "filter";

   String KEY_RESULTS_DIR = "results";

   String KEY_SUITE = "suite.runner";

   String KEY_TEST_RETRIES = "retries";

   @Key( "browser.name" )
   @DefaultValue( "chrome" )
   String browserName();

   /**
    * {@link org.openqa.selenium.Platform}
    * 
    * @return value
    */
   @Key( "browser.platform" )
   @DefaultValue( "linux" )
   String browserPlatform();

   @Key( "browser.version" )
   @DefaultValue( "latest" )
   String browserVersion();

   @Key( "browser.video" )
   @DefaultValue( "true" )
   boolean browserVideo();

   @Key( KEY_FILTER )
   @DefaultValue( "" )
   String filter();

   default Optional<HttpUrl> getSecondaryUrl( int number ) {
      return Optional.ofNullable( HttpUrl.parse( getProperty( "url" + number, "" ) ) );
   }

   @Key( "gridlastic.accessKey" )
   String gridlasticAccessKey();

   @Key( "gridlastic.hubSubdomain" )
   String gridlasticHubSubdomain();

   @Key( "gridlastic.username" )
   String gridlasticUsername();

   default boolean is( String key, String expectedValue ) {
      return nullToEmpty( getProperty( key ) ).equalsIgnoreCase( nullToEmpty( expectedValue ) );
   }

   @Key( "log.handlers" )
   @DefaultValue( "net.ghue.jelenium.impl.log.LogHandlerFactoryStdOut, " +
                  "net.ghue.jelenium.impl.log.LogHandlerFactoryFileDebug, " +
                  "net.ghue.jelenium.impl.log.LogHandlerFactoryFileWarn" )
   List<LogHandlerFactory> logHandlers();

   default String print() {
      // Make sure keys are ordered.
      final Map<String, String> args = new TreeMap<>();
      this.fill( args );
      StringBuilder sb = new StringBuilder();
      sb.append( '\n' ).append( "##### Jelenium Configuration Values: " ).append( '\n' );
      for ( Entry<String, String> arg : args.entrySet() ) {
         sb.append( "  " ).append( arg.getKey() ).append( " = " );
         final String key = arg.getKey().toLowerCase();
         if ( key.contains( "accesskey" ) ||
              key.contains( "password" ) ||
              key.contains( "secret" ) ) {
            sb.append( "***" );
         } else {
            sb.append( arg.getValue() );
         }
         sb.append( '\n' );
      }
      sb.append( '\n' );
      return sb.toString();
   }

   @DefaultValue( "net.ghue.jelenium.impl.ResultsHandlerStdOut" )
   List<TestResultsHandler> resultHandlers();

   @Key( KEY_RESULTS_DIR )
   @DefaultValue( "results" )
   Path results();

   @Key( "action.retry.delay" )
   @DefaultValue( "1000 ms" )
   Duration retryDelay();

   @Key( "action.retry.timeout" )
   @DefaultValue( "10 seconds" )
   Duration retryTimeout();

   @Key( KEY_SUITE )
   Class<JeleniumSuiteRunner> suite();

   @Key( "suite.reuseBrowser" )
   boolean suiteReuseBrowser();

   @Key( "suite.threads" )
   @DefaultValue( "1" )
   int suiteThreads();

   @Key( "suite.webDriverProvider" )
   Class<WebDriverProvider> suiteWdp();

   @Key( KEY_TEST_RETRIES )
   @DefaultValue( "0" )
   int testRetries();

   @DefaultValue( "http://localhost" )
   HttpUrl url();

}
