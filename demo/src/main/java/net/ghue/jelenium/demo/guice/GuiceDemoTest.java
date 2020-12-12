package net.ghue.jelenium.demo.guice;

import javax.inject.Inject;
import net.ghue.jelenium.api.annotation.GuiceModule;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.TestContext;

/**
 * Every test will have its own GUICE {@link com.google.inject.Injector}. You can add your own
 * {@link com.google.inject.Module}'s by annotating the class with one or more
 * {@link GuiceModule}'s.
 */
@GuiceModule( MyGuiceModule.class )
public final class GuiceDemoTest implements JeleniumTest {

   @Inject
   private MyService myService;

   @Override
   public void run( TestContext context ) throws Exception {
      context.log().info().msg( "Stuff: %s", myService.getStuff() ).log();
   }

}
