package net.ghue.jelenium.impl.test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Provider;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.suite.SuiteRunnerDefault;

final class SuiteRunnerProvider implements Provider<JeleniumSuiteRunner> {

   private final JeleniumConfig config;

   @Inject
   SuiteRunnerProvider( JeleniumConfig config ) {
      this.config = config;
   }

   /**
    * Scan CONFIG and the class path looking for the {@link JeleniumSuiteRunner} to use.
    */
   Class<? extends JeleniumSuiteRunner> findTestSuite() throws IOException {
      final Optional<Class<JeleniumSuiteRunner>> suiteFromSettings =
            Optional.ofNullable( this.config.suite() );

      if ( suiteFromSettings.isPresent() ) {
         return suiteFromSettings.get();
      }

      final List<Class<? extends JeleniumSuiteRunner>> suites = Scanner.findSuites();
      if ( suites.isEmpty() ) {
         return SuiteRunnerDefault.class;
      } else if ( suites.size() == 1 ) {
         return suites.get( 0 );
      } else {
         //TODO find default using annotations.
         return suites.get( 0 );
      }
   }

   @Override
   public JeleniumSuiteRunner get() {
      try {
         Class<? extends JeleniumSuiteRunner> suiteClass = findTestSuite();
         final JeleniumSuiteRunner suite = suiteClass.newInstance();
         return suite;
      } catch ( InstantiationException | IllegalAccessException | IOException ex ) {
         throw new RuntimeException( ex );
      }
   }
}
