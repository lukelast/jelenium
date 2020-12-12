package net.ghue.jelenium.demo.action.page;

import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import net.ghue.jelenium.api.action.RetryableAction;
import net.ghue.jelenium.api.page.Page;

public class PageDynamicLoading1 extends Page {

   WebElement blah;

   @FindBy( css = "div#start button" )
   private WebElement startButton;

   public void clickStart() {
      //getDriver().findElement( By.cssSelector( "div#start button" ) ).click();
      this.startButton.click();
   }

   public void go() {
      navigate().to( url -> url.addPathSegment( "dynamic_loading" ).addPathSegment( "1" ) );
   }

   public void verifyFinishUsingAction() {
      getContext().action()
                  .start( driver -> driver.findElement( By.cssSelector( "div#finish h4" ) ) )
                  .add( WebElement::getText )
                  .add( Assertions::assertThat )
                  .add( assertion -> assertion.containsIgnoringCase( "Hello World" ) )
                  .withRetryDelay( Duration.ofSeconds( 1 ) )
                  .withRetryTimeout( Duration.ofSeconds( 8 ) )
                  .buildSimple()
                  .execute();
   }

   @RetryableAction( retryDelaySec = .5, retryTimeoutSec = 2 )
   public void verifyFinishUsingActionMethod() {
      WebElement element = getDriver().findElement( By.cssSelector( "div#finish h4" ) );
      String text = element.getText();
      Assertions.assertThat( text ).containsIgnoringCase( "Hello World" );
   }

   public void verifyFinishUsingWait() {
      getContext().webDriverWait().ignoring( AssertionError.class ).until( wd -> {
         WebElement element = getDriver().findElement( By.cssSelector( "div#finish h4" ) );
         String text = element.getText();
         Assertions.assertThat( text ).containsIgnoringCase( "Hello World" );
         return true;
      } );
   }

   public void verifyHeader() {
      Assertions.assertThat( getDriver().findElement( By.tagName( "h3" ) ).getText() )
                .containsIgnoringCase( "Dynamically Loaded Page Elements" );
   }

}
