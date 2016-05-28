package net.ghue.jelenium.impl;

import java.util.Map;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.TestArgs;

/**
 * This implementation must be immutable because instances are shared between test runs.
 * 
 * @author Luke Last
 */
final class TestArgsImpl implements TestArgs {

   private final Map<String, String> args;

   private final HttpUrl url;

   /**
    * <p>
    * Constructor for TestArgsImpl.
    * </p>
    *
    * @param args a {@link java.util.Map} object.
    */
   public TestArgsImpl( Map<String, String> args ) {
      this.args = ImmutableMap.copyOf( args );
      if ( Strings.isNullOrEmpty( args.get( KEY_URL ) ) ) {
         this.url = defaultUrl();
      } else {
         this.url = HttpUrl.parse( args.get( KEY_URL ) );
         if ( this.url == null ) {
            throw new IllegalArgumentException( "BAD URL: " + args.get( KEY_URL ) );
         }
      }
   }

   private HttpUrl defaultUrl() {
      return new HttpUrl.Builder().scheme( "http" ).host( "localhost" ).build();
   }

   @Override
   public String getArg( String key ) {
      if ( KEY_URL.equals( key ) ) {
         return this.url.toString();
      }
      return Strings.nullToEmpty( this.args.get( key ) );
   }

   @Override
   public HttpUrl getSecondaryUrl( int index ) {
      HttpUrl url = HttpUrl.parse( getArg( KEY_URL + index ) );
      return ( url != null ) ? url : defaultUrl();
   }

   @Override
   public HttpUrl getUrl() {
      return this.url;
   }

}
