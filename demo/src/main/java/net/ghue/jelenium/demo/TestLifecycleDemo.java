package net.ghue.jelenium.demo;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestResult;

/**
 * Demonstrate the available life cycle methods of a test run.
 */
public final class TestLifecycleDemo implements JeleniumTest {

   @Override
   public void onBeforeRun( TestContext context ) throws Exception {
      context.getLog().info( "Before test" );
   }

   @Override
   public void onFail( TestContext context ) throws Exception {
      context.getLog().warn( "Test Failed" );
   }

   @Override
   public void onFinish( TestContext context, TestResult result ) throws Exception {
      context.getLog().info( "Test Passed or Failed %s", result );
   }

   @Override
   public void onPass( TestContext context ) throws Exception {
      context.getLog().info( "Test Passed" );
   }

   @Override
   public void run( TestContext context ) throws Exception {
      context.getLog().info( "Actual Test" );
   }

}
