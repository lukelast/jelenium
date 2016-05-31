package net.ghue.jelenium.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import net.ghue.jelenium.api.SeleniumTest;
import net.ghue.jelenium.api.TestContext;

/**
 * This example is fully contained in this file. Each file/class that implements
 * {@link SeleniumTest} always contains a single test.
 */
public final class StandaloneExampleTest implements SeleniumTest {

   /**
    * The test must have a public no-argument constructor. It is suggested you don't define any
    * constructors. Instead use a life-cycle method defined in {@link SeleniumTest}.
    */
   public StandaloneExampleTest() {}

   /**
    * The entry point for test execution.
    */
   @Override
   public void run( TestContext context ) throws Exception {

      context.getLog().info( "hi" );

      // How to fetch an argument passed in via the MAVEN POM.
      context.getLog().info( "Value of my arg: %s", context.getTestArgs().getArg( "mykey" ) );

      context.getWebNavigate().toPrimaryUrl();

      WebElement header = context.getWebDriver().findElement( By.tagName( "h1" ) );

      context.getLog().info( "H1 tag text: %s", header.getText() );

      context.getLog().info( "goodbye" );
   }

}
