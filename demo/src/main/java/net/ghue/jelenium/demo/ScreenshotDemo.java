package net.ghue.jelenium.demo;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;

public final class ScreenshotDemo implements JeleniumTest {

   @Override
   public void run( TestContext context ) throws Exception {

      context.log().info().msg( "Root results directory: %s", context.getConfig().results() ).log();

      context.log().info().msg( "Test results directory: %s", context.getResultDir() ).log();

      context.getWebNavigate().toPrimaryUrl();

      context.getScreenshotSaver().saveScreenshot( "capture pixels" );
   }
}
