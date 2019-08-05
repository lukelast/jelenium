package net.ghue.jelenium.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestRun;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

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
    * Scan the class path to find all {@link JeleniumTest}'s to run and turn them in to
    * {@link TestRunImpl}'s and filter.
    */
   private List<TestRunImpl> findTests() throws IOException {
      final List<Class<? extends JeleniumTest>> testClasses = Scanner.findTests();

      final List<TestRunImpl> tests =
            testClasses.stream().map( tc -> new TestRunImpl( tc, settings ) ).filter( testRun -> {
               testRun.checkIfSkip();
               return testRun.readyToRun();
            } ).collect( ImmutableList.toImmutableList() );
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

      final List<TestRunImpl> tests = findTests();
      final JeleniumSuiteRunner suite = findTestSuite();
      suite.setSettings( this.settings );

      System.out.println( "Using Test Suite:  " + suite.getClass().getCanonicalName() );
      System.out.println();
      System.out.println( "The following tests were found:\n" +
                          tests.stream()
                               .map( JeleniumTestRun::getName )
                               .map( str -> "  " + str )
                               .collect( Collectors.joining( "\n" ) ) );
      System.out.println();

      suite.runTests( tests );
      suite.processResults( tests );
   }
}
