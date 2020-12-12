package net.ghue.jelenium.demo.action.page;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.google.common.truth.Truth;
import net.ghue.jelenium.api.page.Page;

public final class PageFormAuthSecure extends Page {

   @FindBy( css = "#content > div > h2" )
   private WebElement header;

   @FindBy( css = "#content > div > a" )
   private WebElement logoutButton;

   public void logout() {
      this.logoutButton.click();
   }

   public void verifyHeader() {

      Truth.assertThat( header.getText() ).ignoringCase().contains( "Secure Area" );

      Assertions.assertThat( header.getText() ).isEqualToIgnoringWhitespace( "Secure Area" );

   }
}
