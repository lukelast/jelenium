package net.ghue.jelenium.impl.test;

import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Provider;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.suite.SuiteRunnerDefault;
import net.ghue.jelenium.impl.Utils;

final class SuiteRunnerProvider implements Provider<JeleniumSuiteRunner> {

   private final JeleniumConfig config;

   private final Scanner scanner;

   @Inject
   SuiteRunnerProvider( JeleniumConfig config, Scanner scanner ) {
      this.config = config;
      this.scanner = scanner;
   }

   /**
    * Scan CONFIG and the class path looking for the {@link JeleniumSuiteRunner} to use.
    */
   Class<? extends JeleniumSuiteRunner> findTestSuite() {
      if ( this.config.suite() != null ) {
         return this.config.suite();
      }

      return scanner.findSuites()
                    // Sort so if more than one at least the result is consistent.
                    .sorted( Comparator.comparing( Class::getCanonicalName ) )
                    .findFirst()
                    .orElse( SuiteRunnerDefault.class );
   }

   @Override
   public JeleniumSuiteRunner get() {
      Class<? extends JeleniumSuiteRunner> suiteClass = findTestSuite();
      final JeleniumSuiteRunner suite = Utils.newInstance( suiteClass );
      return suite;
   }
}
