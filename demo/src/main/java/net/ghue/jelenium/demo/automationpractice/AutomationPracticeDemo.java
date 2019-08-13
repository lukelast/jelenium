package net.ghue.jelenium.demo.automationpractice;

import java.time.Duration;
import javax.inject.Inject;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

public class AutomationPracticeDemo implements JeleniumTest {

   @Inject
   PageHome home;

   @Inject
   PageProduct product;

   @Inject
   PageSearch search;

   @Override
   public void run( TestContext context ) throws Exception {

      // Open site.
      home.go();

      // Search site for shirts.
      home.search( "shirt" );

      final String productName = "Faded Short Sleeve T-shirts";

      search.verifyFirstProductName( productName );
      search.clickFirstProduct();
      // We are now on the product page.
      product.verifyProductName( productName );
      // Click the button to add to cart.
      // Then when a confirmation pop-up appears click to go to the cart.
      product.addToCart().continueShopping();

      // Go back to home page.
      home.go();
      home.search( "blouse" );
      search.verifyFirstProductName( "Blouse" );
      search.addFirstToCart().clickProceedToCheckout();

      context.pause( Duration.ofSeconds( 5 ) );
   }

}
