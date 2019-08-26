package net.ghue.jelenium.impl;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.TestResultsHandler;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

/**
 * Takes the raw input arguments and runs all the tests.
 *
 * @author Luke Last
 */
public final class JeleniumRunner {

   private final JeleniumSettings settings;

   private final Provider<JeleniumSuiteRunner> suiteRunnerProvider;

   private final Provider<List<TestResultsHandler>> resultsHandlersProvider;

   private final Provider<TestFactory> testFactoryProvider;

   private final PrintStream out;

   @Inject
   JeleniumRunner( JeleniumSettings settings, Provider<JeleniumSuiteRunner> suiteRunnerProvider,
                   Provider<List<TestResultsHandler>> resultsHandlersProvider,
                   Provider<TestFactory> testFactoryProvider, PrintStream stdOut ) {
      this.settings = settings;
      this.suiteRunnerProvider = suiteRunnerProvider;
      this.resultsHandlersProvider = resultsHandlersProvider;
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
      // Print all settings for debugging.
      out.println( this.settings.toString() );

      final JeleniumSuiteRunner suite = this.suiteRunnerProvider.get();

      out.println( "Using Test Suite:  " + suite.getClass().getCanonicalName() );
      out.println();

      final TestFactory testFactory = this.testFactoryProvider.get();

      if ( !testFactory.getSkippedTests().isEmpty() ) {
         out.println( "The following tests are being skipped because they did not match the filter: '" +
                      settings.getFilter() +
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

      suite.runTests( testFactory.getTestsToRun() );

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

      final List<TestResultsHandler> resultsHandlers = this.resultsHandlersProvider.get();
      for ( TestResultsHandler handler : resultsHandlers ) {
         handler.processResults( testResults );
      }

   }
}
