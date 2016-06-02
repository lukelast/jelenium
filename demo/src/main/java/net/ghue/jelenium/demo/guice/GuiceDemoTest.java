package net.ghue.jelenium.demo.guice;

import javax.inject.Inject;
import com.google.inject.Module;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.annotation.GuiceModule;

/**
 * Every test will have its own GUICE {@link com.google.inject.Injector}. You can add your own
 * {@link Module}'s by annotating the class with one or more {@link GuiceModule}'s.
 */
@GuiceModule( MyGuiceModule.class )
public final class GuiceDemoTest implements JeleniumTest {

   @Inject
   private MyService myService;

   @Override
   public void run( TestContext context ) throws Exception {
      context.getLog().info( "Stuff: %s", myService.getStuff() );
   }

}
