package net.ghue.jelenium.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;

/**
 * Takes the raw input arguments and runs all the tests.
 *
 * @author Luke Last
 */
public final class TestRunnerImpl {

   private final JeleniumSettings settings;

   /**
    * @param parsedArgs a {@link java.util.Map} object.
    */
   public TestRunnerImpl( Map<String, String> parsedArgs ) {
      this.settings = new SettingsImpl( parsedArgs );
   }

   /**
    * Run test suite.
    *
    * @throws java.lang.Exception if any.
    */
   public void run() throws Exception {

      final List<Class<? extends JeleniumTest>> testClasses = Scanner.findTests();

      final WebDriverManager wdc = new WebDriverManager() {

         @Override
         public void close() throws IOException {}

         @Override
         public void giveBack( RemoteWebDriver driver ) {
            driver.quit();
         }

         @Override
         public RemoteWebDriver take() {
            return new ChromeDriver();
         }
      };

      final List<TestRun> tests = testClasses.stream()
                                             .map( tc -> new TestRun( tc, wdc, settings ) )
                                             .collect( ImmutableList.toImmutableList() );

      tests.forEach( TestRun::checkIfSkip );

      tests.stream().filter( TestRun::readyToRun ).forEach( TestRun::run );

      wdc.close();

      final Multiset<TestResult> results =
            tests.stream()
                 .map( TestRun::getResult )
                 .collect( ImmutableMultiset.toImmutableMultiset() );

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
