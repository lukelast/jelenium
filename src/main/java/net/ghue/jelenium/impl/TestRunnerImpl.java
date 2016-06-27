package net.ghue.jelenium.impl;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
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

      final Multiset<TestResult> results = ConcurrentHashMultiset.create();

      final RemoteWebDriver driver = new ChromeDriver();

      final WebDriverManager wdc = new WebDriverManager() {

         @Override
         public void giveBack( RemoteWebDriver driver ) {
            driver.manage().deleteAllCookies();
            driver.navigate().refresh();
         }

         @Override
         public RemoteWebDriver take() {
            return driver;
         }
      };

      for ( Class<? extends JeleniumTest> testClass : testClasses ) {

         try ( TestRun tr = new TestRun( testClass, wdc, settings ) ) {
            tr.run();
            results.add( tr.getResult() );
         }
      }

      driver.quit();

      System.out.println( "\n========== PASSED " +
                          results.count( TestResult.PASSED ) +
                          " FAILED " +
                          results.count( TestResult.FAILED ) +
                          " TOTAL " +
                          results.size() +
                          " ==========\n" );

      if ( 0 < results.count( TestResult.ERROR ) ) {
         throw new Exception( results.count( TestResult.ERROR ) + " tests had an ERROR" );
      }

      if ( 0 < results.count( TestResult.FAILED ) ) {
         throw new Exception( results.count( TestResult.FAILED ) + " tests failed" );
      }
   }

}
