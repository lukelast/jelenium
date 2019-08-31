package net.ghue.jelenium.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.WebDriverProvider;

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
   private Path buildTestResultsDir( String webDriverName, int attempt ) {
      final StringBuilder dirName = new StringBuilder();
      dirName.append( this.name.getShortName() );
      if ( !webDriverName.isEmpty() ) {
         dirName.append( '-' ).append( webDriverName );
      }

      if ( 1 < attempt ) {
         dirName.append( "-try" ).append( attempt );
      }

      Path path;
      int counter = 0;
      do {
         counter++;
         if ( 1 < counter ) {
            dirName.append( '-' ).append( counter );
         }
         path = this.config.results().resolve( dirName.toString() ).toAbsolutePath();
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
   public JeleniumTestResult run( RemoteWebDriver remoteWebDriver,
                                  String webDriverName,
                                  int attempt ) {
      final TestExecution testExec =
            new TestExecution( testClass,
                               config,
                               buildTestResultsDir( webDriverName, attempt ),
                               remoteWebDriver );
      testExec.run();
      final TestResultImpl result = new TestResultImpl( testExec );
      this.results.add( result );
      return result;
   }

   @Override
   public List<JeleniumTestResult> runWithRetries( WebDriverProvider driverProvider ) {
      final Builder<JeleniumTestResult> builder = ImmutableList.builder();

      // Use a max attempts limit to prevent running forever.
      for ( int attempt = 1; attempt < 10; attempt++ ) {

         final RemoteWebDriver webDriver = driverProvider.get();
         final JeleniumTestResult result =
               this.run( webDriver, driverProvider.getWebDriverName(), attempt );
         driverProvider.finished( result, webDriver );
         builder.add( result );

         TestResultImpl resultImpl = (TestResultImpl) result;
         if ( resultImpl.shouldTryAgain( attempt ) ) {
            resultImpl.setRetried( true );
         } else {
            break;// DONE
         }
      }

      return builder.build();
   }

}
