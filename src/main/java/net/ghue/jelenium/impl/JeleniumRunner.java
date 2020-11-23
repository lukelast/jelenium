package net.ghue.jelenium.impl;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.TestResultsHandler;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

/**
 * Takes the raw input arguments and runs all the tests.
 *
 * @author Luke Last
 */
public final class JeleniumRunner {

   private final JeleniumConfig config;

   private final PrintStream out;

   private final Provider<JeleniumSuiteRunner> suiteRunnerProvider;

   private final Provider<TestFactory> testFactoryProvider;

   @Inject
   JeleniumRunner( JeleniumConfig config, Provider<JeleniumSuiteRunner> suiteRunnerProvider,
                   Provider<TestFactory> testFactoryProvider, PrintStream stdOut ) {
      this.config = config;
      this.suiteRunnerProvider = suiteRunnerProvider;
      this.testFactoryProvider = testFactoryProvider;
      this.out = stdOut;
   }

   /**
    * Run test suite.
    *
    * @throws java.lang.Exception if any.
    */
   public void run() throws Exception {

      out.println();
      out.println( "##### STARTING JELENIUM #####" );
      // Print all configuration for debugging.
      out.println( this.config.print() );

      final JeleniumSuiteRunner suite = this.suiteRunnerProvider.get();

      out.println( "Using Test Suite Runner:  " + suite.getClass().getCanonicalName() );
      out.println();

      final TestFactory testFactory = this.testFactoryProvider.get();

      if ( !testFactory.getSkippedTests().isEmpty() ) {
         out.println( "The following tests are being skipped because they did not match the filter: '" +
                      config.filter() +
                      "'" );
         for ( TestManager tm : testFactory.getSkippedTests() ) {
            out.println( "  " + tm.getName().getFullName() );
         }
         out.println();
      }

      out.println( "The following tests will be run:\n" +
                   testFactory.getTestsToRun()
                              .stream()
                              .map( TestManager::getName )
                              .map( str -> "  " + str )
                              .collect( Collectors.joining( "\n" ) ) );
      out.println();

      suite.runTests( testFactory.getTestsToRun(), this.config );

      // Collect all results.
      final List<JeleniumTestResult> testResults = testFactory.getTestsToRun()
                                                              .stream()
                                                              .flatMap( TestManager::getResults )
                                                              .collect( Collectors.toList() );

      // Add skipped tests to results.
      testFactory.getSkippedTests()
                 .stream()
                 .map( vm -> new TestResultSkipped( vm.getName() ) )
                 .forEach( testResults::add );

      for ( TestResultsHandler handler : this.config.resultHandlers() ) {
         handler.processResults( testResults );
      }

   }
}
