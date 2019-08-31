package net.ghue.jelenium.demo.automationpractice;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.google.common.truth.Truth;
import net.ghue.jelenium.api.Page;
import okhttp3.HttpUrl;

public class PageHome extends Page {

   @FindBy( css = "#editorial_block_center > h1" )
   private WebElement automationHeader;

   @FindBy( css = "#search_query_top" )
   private WebElement searchBox;

   @FindBy( css = "#searchbox > button" )
   private WebElement searchButton;

   public void go() {
      navigate().to( HttpUrl.parse( "http://automationpractice.com" ) );
      this.verifyHeader();
   }

   public void search( String text ) {
      this.searchBox.sendKeys( text );
      this.searchButton.click();
   }

   public void verifyHeader() {
      Truth.assertThat( this.automationHeader.getText() )
           .ignoringCase()
           .contains( "Automation Practice Website" );
   }

}
