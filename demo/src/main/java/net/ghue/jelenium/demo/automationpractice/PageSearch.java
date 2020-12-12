package net.ghue.jelenium.demo.automationpractice;

import javax.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.google.common.truth.Truth;
import net.ghue.jelenium.api.Page;
import net.ghue.jelenium.api.action.RetryableAction;

public class PageSearch extends Page {

   @FindBy( css = "#center_column > ul > li > div > div.right-block > div.button-container > a.button.ajax_add_to_cart_button.btn.btn-default" )
   private WebElement addFirstItemToCart;

   @FindBy( css = "#center_column > ul > li > div > div.right-block > h5 > a" )
   private WebElement firstProductName;

   @Inject
   private PagePopupAddedToCart popupAddedToCart;

   @FindBy( css = "#search_query_top" )
   private WebElement searchBox;

   @FindBy( css = "#searchbox > button" )
   private WebElement searchButton;

   public PagePopupAddedToCart addFirstToCart() {

      this.addFirstItemToCart.click();
      this.popupAddedToCart.verifyMessage();
      return this.popupAddedToCart;
   }

   public void clickFirstProduct() {
      this.firstProductName.click();
   }

   @RetryableAction
   public void verifyFirstProductName( String name ) {
      Truth.assertThat( this.firstProductName.getText() ).ignoringCase().contains( name );
   }

}
