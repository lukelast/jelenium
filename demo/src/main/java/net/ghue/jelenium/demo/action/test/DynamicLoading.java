package net.ghue.jelenium.demo.action.test;

import javax.inject.Inject;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.demo.action.page.PageDynamicLoading1;

public final class DynamicLoading implements JeleniumTest {

   @Inject
   private PageDynamicLoading1 pageDynamicLoading1;

   @Override
   public void run( TestContext context ) throws Exception {

      pageDynamicLoading1.go();
      pageDynamicLoading1.verifyHeader();
      pageDynamicLoading1.clickStart();
      pageDynamicLoading1.verifyFinishUsingAction();

      Thread.sleep( 5_000 );
   }

}
