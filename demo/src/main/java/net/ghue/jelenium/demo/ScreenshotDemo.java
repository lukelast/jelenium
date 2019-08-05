package net.ghue.jelenium.demo;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

public final class ScreenshotDemo implements JeleniumTest {

   @Override
   public void run( TestContext context ) throws Exception {

      context.getLog().info( "Root results directory: %s", context.getSettings().getResultsDir() );

      context.getLog().info( "Test results directory: %s", context.getResultDir() );

      context.getWebNavigate().toPrimaryUrl();

      context.getScreenshotSaver().saveScreenshot( "capture pixels" );
   }
}
