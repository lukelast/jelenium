package net.ghue.jelenium.demo;

import java.time.Duration;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestResultState;

/**
 * Demonstrate the available life cycle methods of a test run.
 */
public final class LoggingDemo implements JeleniumTest {

   @Override
   public void onBeforeRun( TestContext context ) throws Exception {
      context.log().info().msg( "Before test" ).log();
   }

   @Override
   public void onFail( TestContext context ) throws Exception {
      context.log().warn().msg( "Test Failed" ).log();
   }

   @Override
   public void onFinish( TestContext context, TestResultState result ) throws Exception {
      context.log().info().msg( "onFinish: %s", result ).log();
   }

   @Override
   public void onPass( TestContext context ) throws Exception {
      context.log().info().msg( "Test Passed" ).log();
   }

   @Override
   public void run( TestContext context ) throws Exception {

      context.log().info().msg( "Start" ).log();
      context.pause( Duration.ofSeconds( 1 ) );
      context.log().info().msg( "Another Message" ).log();

      context.log().debug().msg( "Debug Level" ).log();
      context.log().info().msg( "Info Level" ).log();
      context.log().warn().msg( "Warning Level" ).log();
      context.log().error().msg( "Error Level" ).log();

      context.log()
             .error()
             .ex( new IllegalStateException( "is bad" ) )
             .msg( "Testing exception" )
             .log();
   }
}
