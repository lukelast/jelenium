package net.ghue.jelenium.impl.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.TestManager;
import net.ghue.jelenium.api.suite.WebDriverSession;
import net.ghue.jelenium.api.test.JeleniumTest;
import net.ghue.jelenium.api.test.JeleniumTestResult;
import net.ghue.jelenium.api.test.TestName;

class TestManagerImpl implements TestManager {

   private final JeleniumConfig config;

   private final TestNameImpl name;

   private final List<JeleniumTestResult> results = new CopyOnWriteArrayList<>();

   private final Class<? extends JeleniumTest> testClass;

   TestManagerImpl( Class<? extends JeleniumTest> testClass, JeleniumConfig config ) {
      this.testClass = testClass;
      this.config = config;
      this.name = new TestNameImpl( testClass );
   }

   /**
    * TODO need to handle when the directory already exists or it is a retry, or multiple web
    * drivers.
    */
   Path buildTestResultsDir( String webDriverName, int attempt ) {
      final StringBuilder dirName = new StringBuilder();
      dirName.append( this.name.getShortName() );
      if ( !webDriverName.isEmpty() ) {
         dirName.append( '-' ).append( webDriverName );
      }

      if ( 1 < attempt ) {
         dirName.append( "-try" ).append( attempt );
      }

      int counter = 0;
      Path path;
      do {
         counter++;
         String appendedDirName = dirName.toString();
         if ( 1 < counter ) {
            appendedDirName += "-" + counter;
         }
         path = this.config.results().resolve( appendedDirName ).toAbsolutePath();
      } while ( Files.isDirectory( path ) );

      return path;
   }

   @Override
   public TestName getName() {
      return this.name;
   }

   @Override
   public Stream<JeleniumTestResult> getResults() {
      return this.results.stream();
   }

   boolean isSkipped() {
      if ( !config.filter().isEmpty() &&
           !name.getFullName().toLowerCase().contains( config.filter() ) ) {
         return true;
      }
      return false;
   }

   @Override
   public JeleniumTestResult run( WebDriverSession driver, int attempt ) {
      final TestExecution testExec =
            new TestExecution( testClass,
                               config,
                               buildTestResultsDir( driver.getName(), attempt ),
                               driver );
      testExec.run();
      final JeleniumTestResult result = testExec.toResult();
      this.results.add( result );
      return result;
   }
}
