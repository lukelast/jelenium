package net.ghue.jelenium.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.function.Consumer;
import javax.inject.Inject;
import org.openqa.selenium.WebDriver;
import net.ghue.jelenium.api.HttpUrl.Builder;

public final class WebNavigate {

   @Inject
   private WebDriver driver;

   @Inject
   private HttpUrl url;

   public HttpUrl getCurrentUrl() {
      return HttpUrl.parse( this.driver.getCurrentUrl() );
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

   public void to( HttpUrl url ) {
      driver.navigate().to( url.url() );
   }

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
