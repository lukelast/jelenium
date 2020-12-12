package net.ghue.jelenium.demo.automationpractice;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import net.ghue.jelenium.api.Page;
import net.ghue.jelenium.api.action.RetryableAction;

public class PagePopupAddedToCart extends Page {

   @FindBy( css = "#layer_cart div.button-container span.continue.btn" )
   private WebElement continueShopping;

   @FindBy( css = "#layer_cart > div.clearfix > div.layer_cart_product.col-xs-12.col-md-6 > h2" )
   private WebElement message;

   @FindBy( xpath = "//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/a" )
   private WebElement proceedToCheckout;

   public void clickProceedToCheckout() {
      this.proceedToCheckout.click();
   }

   public void continueShopping() {
      Assertions.assertThat( continueShopping.getText() ).isEqualTo( "Continue shopping" );
      this.continueShopping.click();
      this.verifyMessageEmpty();
   }

   @RetryableAction
   public void verifyMessage() {
      Assertions.assertThat( message.getText() )
                .isEqualTo( "Product successfully added to your shopping cart" );
   }

   @RetryableAction
   public void verifyMessageEmpty() {
      Assertions.assertThat( message.getText() ).isEmpty();
   }
}
