package net.ghue.jelenium.demo.action.test;

import java.time.Duration;
import javax.inject.Inject;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;
import net.ghue.jelenium.demo.action.page.PageDynamicLoading1;

public final class DynamicLoading implements JeleniumTest {

   @Inject
   private PageDynamicLoading1 pageDynamicLoading1;

   @Override
   public void run( TestContext context ) throws Exception {

      pageDynamicLoading1.go();
      pageDynamicLoading1.verifyHeader();
      pageDynamicLoading1.clickStart();

      //pageDynamicLoading1.verifyFinishUsingActionMethod();
      pageDynamicLoading1.verifyFinishUsingAction();
      //pageDynamicLoading1.verifyFinishUsingWait();

      context.pause( Duration.ofSeconds( 1 ) );
   }

}
