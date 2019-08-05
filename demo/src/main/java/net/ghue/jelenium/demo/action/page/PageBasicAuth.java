package net.ghue.jelenium.demo.action.page;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import net.ghue.jelenium.api.Page;

public final class PageBasicAuth extends Page {

   @FindBy( css = "#content > div > p" )
   private WebElement message;

   public void go() {
      navigate().to( url -> url.addPathSegment( "basic_auth" )
                               .username( "admin" )
                               .password( "admin" ) );

      Assertions.assertThat( message.getText() )
                .isEqualToIgnoringCase( "Congratulations! You must have the proper credentials." );
   }

}
