package net.ghue.jelenium.demo.action.page;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import net.ghue.jelenium.api.Page;
import net.ghue.jelenium.api.WebNavigate;

public final class PageDynamicLoading1 extends Page {

   public void clickStart() {
      getDriver().findElement( By.cssSelector( "div#start button" ) ).click();
   }

   @Override
   protected void go( WebNavigate navigate ) {
      navigate.to( url -> url.addPathSegment( "dynamic_loading" ).addPathSegment( "1" ) );
   }

   public void verifyFinishNoAction() {
      WebElement element = getDriver().findElement( By.cssSelector( "div#finish h4" ) );
      String text = element.getText();
      Assertions.assertThat( text ).containsIgnoringCase( "Hello World" );
   }

   public void verifyFinishUsingAction() {
      getContext().action()
                  .start( driver -> driver.findElement( By.cssSelector( "div#finish h4" ) ) )
                  .add( WebElement::getText )
                  .add( Assertions::assertThat )
                  .buildSimple( assertion -> assertion.containsIgnoringCase( "Hello World" ) )
                  .execute();
   }

   public void verifyHeader() {
      Assertions.assertThat( getDriver().findElement( By.tagName( "h3" ) ).getText() )
                .containsIgnoringCase( "Dynamically Loaded Page Elements" );
   }

}
