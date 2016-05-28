package net.ghue.jelenium.impl;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.firefox.FirefoxDriver;
import net.ghue.jelenium.api.SeleniumTest;

public final class TestRunnerImpl {

   public void run( Map<String, String> parsedArgs ) throws Exception {

      final TestArgsImpl testArgs = new TestArgsImpl( parsedArgs );

      final List<Class<? extends SeleniumTest>> testClasses = Scanner.findTests();

      int passed = 0;
      int failed = 0;

      for ( Class<? extends SeleniumTest> testClass : testClasses ) {
         try ( TestRun tr = new TestRun( testClass, new FirefoxDriver(), testArgs ) ) {
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
