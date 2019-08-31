package net.ghue.jelenium;

import java.util.Objects;
import javax.annotation.Nullable;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.ghue.jelenium.impl.GuiceModule;
import net.ghue.jelenium.impl.JeleniumRunner;

/**
 * <p>
 * A test runner main class that can be used as an entry point to run selenium tests.
 * </p>
 *
 * @author Luke Last
 */
public final class Main {

   /**
    * <p>
    * Standard java main method.
    * </p>
    *
    * @param args Command line arguments.
    * @throws java.lang.Exception if any.
    */
   public static void main( @Nullable String[] args ) throws Exception {
      Objects.requireNonNull( args );

      final Injector injector = Guice.createInjector( new GuiceModule( args ) );
      final JeleniumRunner jelenium = injector.getInstance( JeleniumRunner.class );
      jelenium.run();
   }
}
