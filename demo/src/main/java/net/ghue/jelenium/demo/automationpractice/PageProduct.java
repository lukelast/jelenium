package net.ghue.jelenium.demo.automationpractice;

import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import net.ghue.jelenium.api.Page;

public class PageProduct extends Page {

   @FindBy( css = "#add_to_cart > button" )
   private WebElement addCartButton;

   @Inject
   private PopupAddedToCart popupAddedToCart;

   @FindBy( css = "#center_column h1" )
   private WebElement productName;

   public PopupAddedToCart addToCart() {
      this.addCartButton.click();
      this.popupAddedToCart.verifyMessage();
      return this.popupAddedToCart;
   }

   public void verifyProductName( String name ) {
      Assertions.assertThat( productName.getText() ).isEqualTo( name );
   }

}
