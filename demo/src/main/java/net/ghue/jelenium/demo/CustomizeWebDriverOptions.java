package net.ghue.jelenium.demo;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

public final class CustomizeWebDriverOptions implements JeleniumTest {

   /**
    * Override this method to change {@link WebDriver} options before the test is run.
    */
   @Override
   public void manage( Options options ) {
      options.window().setSize( new Dimension( 640, 480 ) );
   }

   /**
    * The entry point for test execution.
    */
   @Override
   public void run( TestContext context ) throws Exception {
      context.log()
             .info()
             .msg( "Browser Window Size: %s", context.getWebDriver().manage().window().getSize() )
             .log();
   }

}
