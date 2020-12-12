package net.ghue.jelenium.demo.action.page;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import net.ghue.jelenium.api.page.Page;

public final class PageFormAuthLogin extends Page {

   @FindBy( css = "h2" )
   private WebElement header;

   @FindBy( css = "#login > button" )
   private WebElement loginButton;

   @FindBy( css = "#password" )
   private WebElement password;

   @FindBy( css = "#username" )
   private WebElement username;

   public void go() {
      navigate().to( url -> url.addPathSegment( "login" ) );
      this.verifyHeader();
   }

   public void login() {
      username.sendKeys( "tomsmith" );
      password.sendKeys( "SuperSecretPassword!" );
      loginButton.click();
   }

   public void verifyHeader() {
      Assertions.assertThat( header.getText() ).isEqualTo( "Login Page" );
   }

}
