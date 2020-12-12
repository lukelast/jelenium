package net.ghue.jelenium.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;

/**
 * <p>
 * This example is fully contained in this file. In normal use you shouldn't use {@link WebDriver}
 * directly in your test.
 * </p>
 * <p>
 * Each file/class that implements {@link JeleniumTest} always contains a single test.
 * </p>
 * <p>
 * The test name is the name of the class.
 * </p>
 */
public final class StandaloneDemo implements JeleniumTest {

   /**
    * The test must have a public no-argument constructor. It is suggested you don't define any
    * constructors. Instead use a life-cycle method defined in {@link JeleniumTest}.
    */
   public StandaloneDemo() {}

   /**
    * The entry point for test execution.
    */
   @Override
   public void run( TestContext context ) throws Exception {

      context.log().info().msg( "hi" ).log();

      // How to fetch an argument passed in via the MAVEN POM.
      context.log()
             .info()
             .msg( "Value of my arg: %s", context.getConfig().getProperty( "mykey", "" ) )
             .log();

      context.getWebNavigate().toPrimaryUrl();

      WebElement header = context.getWebDriver().findElement( By.tagName( "h1" ) );

      context.log().info().msg( "H1 tag text: %s", header.getText() ).log();

      context.log().info().msg( "goodbye" ).log();
   }

}
