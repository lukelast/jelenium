package net.ghue.jelenium.impl.config;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.common.collect.ImmutableMap;
import com.google.common.truth.Truth;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.demo.SampleSuite;
import net.ghue.jelenium.impl.config.ConfigFactoryProvider;

class JeleniumConfigTest {

   private static final String HTTP_URL = "http://www.example.com/";

   private JeleniumConfig create( Map<String, String> args ) {
      return new ConfigFactoryProvider().get().create( JeleniumConfig.class, args );
   }

   private JeleniumConfig create( String key, String value ) {
      return create( ImmutableMap.of( key, value ) );
   }

   @Test
   void envVars() {
      //      final JeleniumSettingsBuilder builder =
      //            new JeleniumSettingsBuilder().parseEnvVars( ImmutableMap.of( "jelenium.retries",
      //                                                                         "123" ) );
      //
      //      Assertions.assertThat( builder.getTestRetries() ).isEqualTo( 123 );
      //      Assertions.assertThat( new SettingsImpl( builder ).getTestRetries() ).isEqualTo( 123 );
   }

   @Test
   void systemProps() {
      //      System.setProperty( "jelenium.retries", "123" );
      //      final JeleniumSettingsBuilder builder = new JeleniumSettingsBuilder().parseSystemProps();
      //
      //      Assertions.assertThat( builder.getTestRetries() ).isEqualTo( 123 );
      //      Assertions.assertThat( new SettingsImpl( builder ).getTestRetries() ).isEqualTo( 123 );
   }

   @Test
   void testGet() {
      String key = "key-abc";
      String value = "abcdefg";
      Assertions.assertThat( create( key, value ).getProperty( key ) ).isEqualTo( value );
   }

   @Test
   void testGetFilter() {
      String filter = "abcdefg";
      Assertions.assertThat( create( "filter", filter ).filter() ).isEqualTo( filter );
   }

   @Test
   void testGetResultsDir() {
      Path dir = Paths.get( "/", "a", "b", "c" );
      Assertions.assertThat( create( JeleniumConfig.KEY_RESULTS_DIR, dir.toString() ).results() )
                .isEqualTo( dir );
   }

   @Test
   void testGetRetryDelay() {
      Truth.assertThat( create( "action.retry.delay", "99 seconds" ).retryDelay() )
           .isEqualTo( Duration.ofSeconds( 99 ) );
   }

   @Test
   void testGetRetryTimeout() {
      Truth.assertThat( create( "action.retry.timeout", "3000 ms" ).retryTimeout() )
           .isEqualTo( Duration.ofSeconds( 3 ) );
   }

   @Test
   void testGetRetryTimeoutDefault() {
      Truth.assertThat( create( of() ).retryTimeout() ).isEqualTo( Duration.ofSeconds( 10 ) );
   }

   @Test
   void testGetSecondaryUrl() {
      Assertions.assertThat( create( "url2", HTTP_URL ).getSecondaryUrl( 2 ) )
                .get()
                .asString()
                .isEqualTo( HTTP_URL );
   }

   @Test
   void testGetSecondaryUrl3() {
      Assertions.assertThat( create( "url3", HTTP_URL ).getSecondaryUrl( 3 ) )
                .get()
                .asString()
                .isEqualTo( HTTP_URL );
   }

   @Test
   void testGetSecondaryUrlEmpty() {
      Assertions.assertThat( create( of() ).getSecondaryUrl( -1 ) ).isEmpty();
      Assertions.assertThat( create( of() ).getSecondaryUrl( 0 ) ).isEmpty();
      Assertions.assertThat( create( of() ).getSecondaryUrl( 1 ) ).isEmpty();
   }

   @Test
   void testGetUrl() {
      JeleniumConfig settings = create( "url", HTTP_URL );
      assertThat( settings.url() ).isNotNull();
      assertThat( settings.url().toString() ).isEqualTo( HTTP_URL );
   }

   @Test
   void testJeleniumSuiteRunner() {
      Assertions.assertThat( create( JeleniumConfig.KEY_SUITE,
                                     SampleSuite.class.getCanonicalName() ).suite() )
                .isEqualTo( SampleSuite.class );
   }

   @Test
   void testRetries() {
      Assertions.assertThat( create( of() ).testRetries() ).isEqualTo( 0 );
      Assertions.assertThat( create( JeleniumConfig.KEY_TEST_RETRIES, "1" ).testRetries() )
                .isEqualTo( 1 );
   }

}
