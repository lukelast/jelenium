package net.ghue.jelenium.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.openqa.selenium.WebDriver;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;

/**
 * <p>
 * WebNavigate class.
 * </p>
 *
 * @author Luke Last
 */
public final class WebNavigate {

   @Inject
   private WebDriver driver;

   @Inject
   private HttpUrl url;

   /**
    * <p>
    * getCurrentUrl.
    * </p>
    *
    * @return a {@link HttpUrl} object.
    */
   public HttpUrl getCurrentUrl() {
      final HttpUrl parsed = HttpUrl.parse( this.driver.getCurrentUrl() );
      return ( parsed != null ) ? parsed
            : new HttpUrl.Builder().scheme( "http" ).host( "localhost" ).build();
   }

   /**
    * Navigate to the primary URL while giving you a chance to customize the URL.
    *
    * @param urlModifier Where you want to go.
    */
   public void to( Consumer<HttpUrl.Builder> urlModifier ) {
      Builder builder = this.url.newBuilder();
      urlModifier.accept( builder );
      this.driver.navigate().to( builder.build().url() );
   }

   /**
    * Navigate to the given URL.
    *
    * @param destUrl a {@link HttpUrl} object.
    */
   public void to( @Nullable HttpUrl destUrl ) {
      Objects.requireNonNull( destUrl );
      driver.navigate().to( destUrl.url() );
   }

   /**
    * Navigate the {@link WebDriver} to the configured primary URL.
    */
   public void toPrimaryUrl() {
      to( this.url );
   }

   /**
    * Throws an assertion exception if the current URL is not using HTTPS.
    */
   public void verifyHttps() {
      assertThat( this.getCurrentUrl().scheme() ).isEqualTo( "https" );
   }

}
