package net.ghue.jelenium.demo;

import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;
import net.ghue.jelenium.api.test.TestResultState;

/**
 * Demonstrate the available life cycle methods of a test run.
 */
public final class TestLifecycleDemo implements JeleniumTest {

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
      context.log().info().msg( "Test Passed or Failed %s", result ).log();
   }

   @Override
   public void onPass( TestContext context ) throws Exception {
      context.log().info().msg( "Test Passed" ).log();
   }

   @Override
   public void run( TestContext context ) throws Exception {
      context.log().info().msg( "Actual Test" ).log();
   }

}
