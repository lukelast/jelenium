package net.ghue.jelenium.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;
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

   /**
    * @param parsedArgs a {@link java.util.Map} object.
    */
   public JeleniumRunner( Map<String, String> parsedArgs ) {
      this.settings = createSettings( parsedArgs );
   }

   private JeleniumSettings createSettings( Map<String, String> parsedArgs ) {
      //TODO Allow adding of default values.
      return new SettingsImpl( parsedArgs );
   }

   /**
    * Scan the class path to find all {@link JeleniumTest}'s to run and turn them in to
    * {@link TestExecution}'s and filter.
    */
   private List<TestManagerImpl> findTests() throws IOException {
      final List<Class<? extends JeleniumTest>> testClasses = Scanner.findTests();

      final List<TestManagerImpl> tests =
            testClasses.stream()
                       .map( tc -> new TestManagerImpl( tc, settings ) )
                       .collect( ImmutableList.toImmutableList() );
      return tests;
   }

   /**
    * Scan settings and the class path looking for the {@link JeleniumSuiteRunner} to use.
    */
   private JeleniumSuiteRunner findTestSuite() throws InstantiationException,
                                               IllegalAccessException,
                                               IOException {
      final Optional<Class<JeleniumSuiteRunner>> suiteFromSettings = this.settings.getSuite();

      if ( suiteFromSettings.isPresent() ) {
         return suiteFromSettings.get().newInstance();
      }

      final List<Class<? extends JeleniumSuiteRunner>> suites = Scanner.findSuites();
      if ( suites.isEmpty() ) {
         return new DefaultTestSuite();
      } else if ( suites.size() == 1 ) {
         return suites.get( 0 ).newInstance();
      } else {
         //TODO find default using annotations.
         return suites.get( 0 ).newInstance();
      }
   }

   /**
    * Run test suite.
    *
    * @throws java.lang.Exception if any.
    */
   public void run() throws Exception {

      System.out.println();
      System.out.println( "##### STARTING JELENIUM #####" );
      // Print all settings for debugging.
      System.out.println( this.settings.toString() );

      final List<TestManagerImpl> testManagers = findTests();
      final JeleniumSuiteRunner suite = findTestSuite();
      suite.setSettings( this.settings );

      System.out.println( "Using Test Suite:  " + suite.getClass().getCanonicalName() );
      System.out.println();
      System.out.println( "The following tests were found:\n" +
                          testManagers.stream()
                                      .map( TestManagerImpl::getName )
                                      .map( str -> "  " + str )
                                      .collect( Collectors.joining( "\n" ) ) );
      System.out.println();

      final List<TestManager> testsToRun = new ArrayList<>();
      final List<TestManager> skipped = new ArrayList<>();

      for ( TestManagerImpl tm : testManagers ) {
         if ( tm.isSkipped() ) {
            skipped.add( tm );
         } else {
            testsToRun.add( tm );
         }
      }

      if ( !skipped.isEmpty() ) {
         System.out.println( "The following tests are being skipped because they did not match the filter: '" +
                             settings.getFilter() +
                             "'" );
         for ( TestManager tm : skipped ) {
            System.out.println( "  " + tm.getName().getFullName() );
         }
         System.out.println();
      }

      suite.runTests( testsToRun );

      // Collect all results.
      final List<JeleniumTestResult> testResults =
            testsToRun.stream().flatMap( TestManager::getResults ).collect( Collectors.toList() );

      // Add skipped tests to results.
      skipped.stream()
             .map( vm -> new TestResultSkipped( vm.getName() ) )
             .forEach( testResults::add );

      // TODO Allow adding/changing handlers.
      final TestResultsHandler resultsHandler = new TestResultsHandlerStdOut();
      resultsHandler.processResults( testResults );
   }
}
