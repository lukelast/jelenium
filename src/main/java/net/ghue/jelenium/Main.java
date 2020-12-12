package net.ghue.jelenium;

/**
 * <p>
 * A test runner main class that can be used as an entry point to run SELENIUM tests.
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
   public static void main( String[] args ) throws Exception {
      new JeleniumRunnerBuilder().withArgs( args ).build().run();
   }
}
