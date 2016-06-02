package net.ghue.jelenium.impl;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.firefox.FirefoxDriver;
import net.ghue.jelenium.api.JeleniumTest;

/**
 * Takes the raw input arguments and runs all the tests.
 *
 * @author Luke Last
 */
public final class TestRunnerImpl {

   /**
    * Run test suite.
    *
    * @param parsedArgs a {@link java.util.Map} object.
    * @throws java.lang.Exception if any.
    */
   public void run( Map<String, String> parsedArgs ) throws Exception {

      final SettingsImpl settings = new SettingsImpl( parsedArgs );

      final List<Class<? extends JeleniumTest>> testClasses = Scanner.findTests();

      int passed = 0;
      int failed = 0;

      for ( Class<? extends JeleniumTest> testClass : testClasses ) {
         try ( TestRun tr = new TestRun( testClass, new FirefoxDriver(), settings ) ) {
            tr.run();
            if ( tr.passed() ) {
               passed++;
            } else {
               failed++;
            }
         }
      }

      System.out.println( "\n========== PASSED " +
                          passed +
                          " FAILED " +
                          failed +
                          " TOTAL " +
                          ( passed + failed ) +
                          " ==========\n" );

      if ( 0 < failed ) {
         throw new Exception( failed + " tests failed" );
      }
   }

}
