package net.ghue.jelenium.impl;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import net.ghue.jelenium.api.*;
import net.ghue.jelenium.api.action.RetryableAction;
import net.ghue.jelenium.api.annotation.GuiceModule;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.api.ex.SkipTestException;
import net.ghue.jelenium.impl.action.ActionMethodInterceptor;

final class TestRunImpl implements JeleniumTestRun {

   private Optional<Injector> injector = empty();

   private final TestLog log;

   private final TestName name;

   private volatile TestResult result = TestResult.NOT_RUN;

   private final JeleniumSettings settings;

   private final Class<? extends JeleniumTest> testClass;

   private Optional<JeleniumTest> testInstance = empty();

   private final Path testResultsDir;

   private Optional<RemoteWebDriver> webDriver = empty();

   /**
    * <p>
    * Constructor.
    * </p>
    *
    * @param testClass a {@link java.lang.Class} object.
    * @param webDriver a {@link org.openqa.selenium.WebDriver} object.
    * @param settings a {@link net.ghue.jelenium.api.JeleniumSettings} object.
    */
   TestRunImpl( Class<? extends JeleniumTest> testClass, JeleniumSettings settings ) {
      this.testClass = requireNonNull( testClass );
      this.settings = requireNonNull( settings );
      this.name = new TestNameImpl( testClass );
      this.testResultsDir = createTestResultsDir( settings, name );
      this.log = new TestLogImpl( testResultsDir );
   }

   void checkIfSkip() {
      if ( !settings.getFilter().isEmpty() &&
           !name.getFullName().toLowerCase().contains( settings.getFilter() ) ) {
         this.result = TestResult.SKIPPED;
         log.info( "Skipping %s because it did not match filter", name );
      }
   }

   private Injector createGuiceInjector() {
      final List<Module> modules = new ArrayList<>();

      for ( GuiceModule guiceModAnnotation : testClass.getDeclaredAnnotationsByType( GuiceModule.class ) ) {
         try {
            modules.add( guiceModAnnotation.value().newInstance() );
         } catch ( InstantiationException | IllegalAccessException ex ) {
            ex.printStackTrace();
         }
      }

      modules.add( new AbstractModule() {

         @Override
         protected void configure() {
            bindInterceptor( Matchers.subclassesOf( Page.class ),
                             Matchers.annotatedWith( RetryableAction.class ),
                             new ActionMethodInterceptor( getProvider( TestContext.class ) ) );
            bind( testClass ).in( Singleton.class );
            bind( WebDriver.class ).toInstance( webDriver.get() );
            bind( RemoteWebDriver.class ).toInstance( webDriver.get() );
            bind( TestLog.class ).toInstance( log );
            bind( JeleniumSettings.class ).toInstance( settings );
            bind( ScreenshotSaver.class ).to( ScreenshotSaverImpl.class );
            bind( TestContext.class ).to( TestContextImpl.class );
            bind( HttpUrl.class ).toInstance( settings.getUrl() );
            bind( TestName.class ).toInstance( name );
            bind( Path.class ).annotatedWith( TestResultDir.class ).toInstance( testResultsDir );
         }
      } );
      return Guice.createInjector( modules );
   }

   /**
    * TODO need to handle when the directory already exists or it is a retry, or multiple web
    * drivers.
    */
   private Path createTestResultsDir( JeleniumSettings jelSettings, TestName testName ) {
      return jelSettings.getResultsDir().resolve( testName.getFullName() ).toAbsolutePath();
   }

   private void finish() {
      this.testInstance.ifPresent( jt -> {

         if ( this.result == TestResult.PASSED ) {
            log.info( "Passed: %s", name );
            try {
               jt.onPass( getContext() );
            } catch ( Throwable ex ) {
               log.error( "", ex );
            }
         }

         if ( this.result == TestResult.FAILED ) {
            log.warn( "Failed: %s", name );
            try {
               jt.onFail( getContext() );
               // Take a screenshot at the end on failure.
               getContext().getScreenshotSaver().saveScreenshot( "failed" );
            } catch ( Throwable ex ) {
               log.error( "", ex );
            }
         }

         try {
            jt.onFinish( getContext(), this.result );
         } catch ( Exception ex ) {
            log.error( "", ex );
         }

      } );
   }

   private TestContext getContext() {
      return injector.orElseThrow( () -> new IllegalStateException( "GUICE injector not created yet" ) )
                     .getInstance( TestContext.class );
   }

   @Override
   public TestName getName() {
      return this.name;
   }

   @Override
   public TestResult getResult() {
      return requireNonNull( this.result );
   }

   boolean readyToRun() {
      return this.result == TestResult.NOT_RUN;
   }

   /**
    * Run the test.
    */
   @Override
   public void run( RemoteWebDriver remoteWebDriver ) {
      log.info( "\nStarting Test: %s", name );

      try {
         this.webDriver = of( remoteWebDriver );
         // Web driver must be set before creating injector.
         this.injector = of( createGuiceInjector() );
         this.testInstance = of( injector.get().getInstance( testClass ) );

         final Options manage = this.webDriver.get().manage();
         manage.timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
         manage.timeouts().pageLoadTimeout( 20, TimeUnit.SECONDS );
         manage.timeouts().setScriptTimeout( 20, TimeUnit.SECONDS );
         testInstance.get().manage( manage );

      } catch ( Exception ex ) {
         if ( !this.injector.isPresent() ) {
            this.result = TestResult.ERROR;
            log.error( "Failed to create guice injector", ex );
         } else if ( !this.testInstance.isPresent() ) {
            this.result = TestResult.ERROR;
            log.error( "Failed to instantiate test class " + testClass.getName(),
                       new IllegalStateException( "Failed to instantiate " + testClass, ex ) );
         } else {
            log.error( "", ex );
            this.result = TestResult.FAILED;
         }
      }

      this.testInstance.map( this::runTest ).ifPresent( tr -> this.result = tr );

      this.finish();
   }

   private TestResult runTest( JeleniumTest test ) {
      try {

         if ( test.skipTest() ) {
            throw new SkipTestException( "Statically skipped" );
         }

         test.onBeforeRun( getContext() );

         final int attempts = Math.max( test.retries() + 1, 1 );

         Optional<Exception> lastEx = empty();
         for ( int attempt = 1; attempt <= attempts; attempt++ ) {
            try {
               test.run( getContext() );
               return TestResult.PASSED;
            } catch ( SkipTestException ex ) {
               throw ex;
            } catch ( Exception ex ) {
               lastEx = of( ex );
            }
         }
         throw lastEx.orElse( new RuntimeException() );

      } catch ( SkipTestException ex ) {

         log.info( "Skipping test: %s because %s", name, ex.getMessage() );
         return TestResult.SKIPPED;

      } catch ( Throwable ex ) {

         log.error( "", ex );
         return TestResult.FAILED;
      }
   }

}
