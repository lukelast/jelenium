package net.ghue.jelenium.impl;

import static com.google.common.truth.Truth.assertThat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.common.collect.ImmutableMap;
import com.google.common.truth.Truth;
import net.ghue.jelenium.api.JeleniumSettings;

class SettingsImplTest {

   private static final String HTTP_URL = "http://www.example.com/";

   @Test
   void testGet() {
      String key = "key-abc";
      String value = "abcdefg";
      SettingsImpl settings = new SettingsImpl( ImmutableMap.of( key, value ) );
      Assertions.assertThat( settings.get( key ) ).isEqualTo( value );
   }

   @Test
   void testGetFilter() {
      String filter = "abcdefg";
      SettingsImpl settings =
            new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_FILTER, filter ) );
      Assertions.assertThat( settings.getFilter() ).isEqualTo( filter );
   }

   @Test
   void testGetResultsDir() {
      Path dir = Paths.get( "/", "a", "b", "c" );
      SettingsImpl settings =
            new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_RESULTS_DIR, dir.toString() ) );
      Assertions.assertThat( settings.getResultsDir() ).isEqualTo( dir );
   }

   @Test
   void testGetRetryDelay() {
      Truth.assertThat( new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_RETRY_DELAY,
                                                           "99000" ) ).getRetryDelay() )
           .isEqualTo( Duration.ofSeconds( 99 ) );
   }

   @Test
   void testGetRetryTimeout() {
      Truth.assertThat( new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_RETRY_TIMEOUT,
                                                           "3000" ) ).getRetryTimeout() )
           .isEqualTo( Duration.ofSeconds( 3 ) );
   }

   @Test
   void testGetRetryTimeoutDefault() {
      Truth.assertThat( new SettingsImpl( ImmutableMap.of() ).getRetryTimeout() )
           .isEqualTo( JeleniumSettings.DEFAULT_RETRY_TIMEOUT );

      Truth.assertThat( new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_RETRY_TIMEOUT,
                                                           "-1" ) ).getRetryTimeout() )
           .isEqualTo( JeleniumSettings.DEFAULT_RETRY_TIMEOUT );

      Truth.assertThat( new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_RETRY_TIMEOUT,
                                                           "0" ) ).getRetryTimeout() )
           .isEqualTo( JeleniumSettings.DEFAULT_RETRY_TIMEOUT );
   }

   @Test
   void testGetSecondaryUrl() {
      SettingsImpl settings =
            new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_URL + 2, HTTP_URL ) );
      Assertions.assertThat( settings.getSecondaryUrl( 2 ) ).get().asString().isEqualTo( HTTP_URL );
   }

   @Test
   void testGetSecondaryUrl3() {
      SettingsImpl settings =
            new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_URL + 3, HTTP_URL ) );
      Assertions.assertThat( settings.getSecondaryUrl( 3 ) ).get().asString().isEqualTo( HTTP_URL );
   }

   @Test
   void testGetSecondaryUrlEmpty() {
      Assertions.assertThat( new SettingsImpl( ImmutableMap.of() ).getSecondaryUrl( -1 ) )
                .isEmpty();
      Assertions.assertThat( new SettingsImpl( ImmutableMap.of() ).getSecondaryUrl( 0 ) ).isEmpty();
      Assertions.assertThat( new SettingsImpl( ImmutableMap.of() ).getSecondaryUrl( 1 ) ).isEmpty();
   }

   @Test
   void testGetUrl() {
      SettingsImpl settings =
            new SettingsImpl( ImmutableMap.of( JeleniumSettings.KEY_URL, HTTP_URL ) );
      assertThat( settings.getUrl() ).isNotNull();
      assertThat( settings.getUrl().toString() ).isEqualTo( HTTP_URL );
   }

}
