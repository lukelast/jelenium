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
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestLog;
import net.ghue.jelenium.api.annotation.GuiceModule;
import net.ghue.jelenium.api.annotation.TestName;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.api.ex.SkipTestException;

final class TestRun {

   private Optional<Injector> injector = empty();

   private final TestLog log = new StandardOutLog();

   private final String name;

   private volatile TestResult result = TestResult.NOT_RUN;

   private final JeleniumSettings settings;

   private final Class<? extends JeleniumTest> testClass;

   private Optional<JeleniumTest> testInstance = empty();

   private Optional<RemoteWebDriver> webDriver = empty();

   private final WebDriverManager webDriverManager;

   /**
    * <p>
    * Constructor for TestRun.
    * </p>
    *
    * @param testClass a {@link java.lang.Class} object.
    * @param webDriver a {@link org.openqa.selenium.WebDriver} object.
    * @param settings a {@link net.ghue.jelenium.api.JeleniumSettings} object.
    */
   TestRun( Class<? extends JeleniumTest> testClass, WebDriverManager webDriverProvider,
            JeleniumSettings settings ) {
      this.testClass = requireNonNull( testClass );
      this.webDriverManager = requireNonNull( webDriverProvider );
      this.settings = requireNonNull( settings );
      this.name = testClass.getName();
   }

   void checkIfSkip() {
      if ( !settings.getFilter().isEmpty() &&
           !name.toLowerCase().contains( settings.getFilter() ) ) {
         this.result = TestResult.SKIPPED;
         log.info( "Skipping %s because it did not match filter", name );
      }
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

      this.webDriver.ifPresent( rwd -> {
         try {
            webDriverManager.giveBack( rwd );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      } );
   }

   private TestContext getContext() {
      return injector.orElseThrow( () -> new IllegalStateException( "GUICE injector not created yet" ) )
                     .getInstance( TestContext.class );
   }

   TestResult getResult() {
      return requireNonNull( this.result );
   }

   boolean readyToRun() {
      return this.result == TestResult.NOT_RUN;
   }

   /**
    * Run the test.
    */
   void run() {
      log.info( "Starting Test: %s", name );

      try {
         final List<Module> modules = new ArrayList<>();

         for ( GuiceModule guiceModAnnotation : testClass.getDeclaredAnnotationsByType( GuiceModule.class ) ) {
            try {
               modules.add( guiceModAnnotation.value().newInstance() );
            } catch ( InstantiationException | IllegalAccessException ex ) {
               ex.printStackTrace();
            }
         }

         this.webDriver = of( this.webDriverManager.take() );

         modules.add( new AbstractModule() {

            @Override
            protected void configure() {
               bind( testClass ).in( Singleton.class );
               bind( WebDriver.class ).toInstance( webDriver.get() );
               bind( RemoteWebDriver.class ).toInstance( webDriver.get() );
               bind( TestLog.class ).toInstance( log );
               bind( JeleniumSettings.class ).toInstance( settings );
               bind( TestContext.class ).to( TestContextImpl.class );
               bind( HttpUrl.class ).toInstance( settings.getUrl() );
               bind( String.class ).annotatedWith( TestName.class ).toInstance( name );
               bind( Path.class ).annotatedWith( TestResultDir.class )
                                 .toInstance( settings.getResultsDir()
                                                      .resolve( name )
                                                      .toAbsolutePath() );
            }
         } );

         this.injector = of( Guice.createInjector( modules ) );
         this.testInstance = of( injector.get().getInstance( testClass ) );

         final Options manage = this.webDriver.get().manage();
         manage.timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
         manage.timeouts().pageLoadTimeout( 20, TimeUnit.SECONDS );
         manage.timeouts().setScriptTimeout( 20, TimeUnit.SECONDS );
         testInstance.get().manage( manage );

      } catch ( Exception ex ) {
         if ( !this.injector.isPresent() ) {
            this.result = TestResult.ERROR;
            log.error( "Failed to create guice injector",
                       new IllegalStateException( "Failed to create guice injector" ) );
         } else if ( !this.testInstance.isPresent() ) {
            this.result = TestResult.ERROR;
            log.error( "Failed to instantiate test class " +
                       testClass.getName(),
                       new IllegalStateException( "Failed to instantiate " + testClass ) );
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
