package net.ghue.jelenium.demo.action.test;

import javax.inject.Inject;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;
import net.ghue.jelenium.demo.action.page.PageBasicAuth;
import net.ghue.jelenium.demo.action.page.PageFormAuthLogin;
import net.ghue.jelenium.demo.action.page.PageFormAuthSecure;

public final class Authentication implements JeleniumTest {

   @Inject
   private PageBasicAuth pageBasicAuth;

   @Inject
   private PageFormAuthLogin pageLogin;

   @Inject
   private PageFormAuthSecure pageSecure;

   @Override
   public void run( TestContext context ) throws Exception {
      pageBasicAuth.go();

      // Login to a secure section using a form.
      pageLogin.go();
      pageLogin.login();

      pageSecure.verifyHeader();
      pageSecure.logout();

      pageLogin.verifyHeader();
   }

}
