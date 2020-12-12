package net.ghue.jelenium.demo.automationpractice;

import java.time.Duration;
import javax.inject.Inject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver.Options;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;

public class JtestAutomationPractice implements JeleniumTest {

   @Inject
   PageHome home;

   @Inject
   PageProduct product;

   @Inject
   PageSearch search;

   @Override
   public void manage( Options options ) {
      // Limit the window width because the UI will change at larger resolutions.
      options.window().setSize( new Dimension( 1024, 800 ) );
   }

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

      context.pause( Duration.ofSeconds( 2 ) );
   }

}
