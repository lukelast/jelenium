package net.ghue.jelenium.impl.config;

import static com.google.common.truth.Truth.assertThat;
import org.junit.jupiter.api.Test;
import com.google.common.truth.MapSubject;
import net.ghue.jelenium.impl.config.ConfigProvider;

class ConfigProviderTest {

   private static MapSubject parse( String... args ) {
      return assertThat( ConfigProvider.parseMainArgs( args ) );
   }

   @Test
   void testMain() {
      assertThat( ConfigProvider.parseMainArgs( new String[] {} ) ).isEmpty();
      parse( "" ).isEmpty();
      parse( "blah" ).isEmpty();
      parse( "=value" ).isEmpty();
      parse( "key=" ).containsExactly( "key", "" );
      parse( "key= " ).containsExactly( "key", " " );
      parse( "key=value" ).containsExactly( "key", "value" );
      parse( "key=value=2" ).containsExactly( "key", "value=2" );
      parse( "key1=value1", "key2=value2" ).containsExactly( "key1", "value1", "key2", "value2" );
   }
}
