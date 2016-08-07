package net.ghue.jelenium.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

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

      context.getLog().info( "hi" );

      // How to fetch an argument passed in via the MAVEN POM.
      context.getLog().info( "Value of my arg: %s", context.getSettings().get( "mykey" ) );

      context.getWebNavigate().toPrimaryUrl();

      WebElement header = context.getWebDriver().findElement( By.tagName( "h1" ) );

      context.getLog().info( "H1 tag text: %s", header.getText() );

      context.getLog().info( "goodbye" );
   }

}