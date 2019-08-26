package net.ghue.jelenium.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Provider;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

final class SuiteRunnerProvider implements Provider<JeleniumSuiteRunner> {

   private final JeleniumSettings settings;

   @Inject
   SuiteRunnerProvider( JeleniumSettings settings ) {
      this.settings = settings;
   }

   @Override
   public JeleniumSuiteRunner get() {
      try {
         Class<? extends JeleniumSuiteRunner> suiteClass = findTestSuite();
         final JeleniumSuiteRunner suite = suiteClass.newInstance();
         suite.setSettings( this.settings );
         return suite;
      } catch ( InstantiationException | IllegalAccessException | IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   /**
    * Scan settings and the class path looking for the {@link JeleniumSuiteRunner} to use.
    */
   Class<? extends JeleniumSuiteRunner> findTestSuite() throws IOException {
      final Optional<Class<JeleniumSuiteRunner>> suiteFromSettings = this.settings.getSuite();

      if ( suiteFromSettings.isPresent() ) {
         return suiteFromSettings.get();
      }

      final List<Class<? extends JeleniumSuiteRunner>> suites = Scanner.findSuites();
      if ( suites.isEmpty() ) {
         return DefaultTestSuite.class;
      } else if ( suites.size() == 1 ) {
         return suites.get( 0 );
      } else {
         //TODO find default using annotations.
         return suites.get( 0 );
      }
   }
}
