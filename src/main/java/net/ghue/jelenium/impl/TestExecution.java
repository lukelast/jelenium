package net.ghue.jelenium.impl;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.Page;
import net.ghue.jelenium.api.ScreenshotSaver;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultState;
import net.ghue.jelenium.api.action.RetryableAction;
import net.ghue.jelenium.api.annotation.GuiceModule;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.ex.SkipTestException;
import net.ghue.jelenium.api.log.TestLog;
import net.ghue.jelenium.impl.action.ActionMethodInterceptor;
import net.ghue.jelenium.impl.log.TestLogImpl;
import okhttp3.HttpUrl;

/**
 * Performs one run of a test.
 * 
 * @author Luke Last
 */
final class TestExecution {

   private final JeleniumConfig config;

   private Optional<Injector> injector = empty();

   private final TestLogImpl log;

   final TestName name;

   volatile TestResultState result = TestResultState.NOT_RUN;

   private final Instant startTime = Instant.now();

   private final Class<? extends JeleniumTest> testClass;

   private Optional<JeleniumTest> testInstance = empty();

   private final Path testResultsDir;

   private final RemoteWebDriver webDriver;

   /**
    * <p>
    * Constructor.
    * </p>
    *
    * @param testClass a {@link java.lang.Class} object.
    * @param webDriver a {@link org.openqa.selenium.WebDriver} object.
    * @param config a {@link net.ghue.jelenium.api.config.JeleniumSettings} object.
    */
   TestExecution( Class<? extends JeleniumTest> testClass, JeleniumConfig config,
                  Path testResultsDir, RemoteWebDriver webDriver ) {
      this.testClass = requireNonNull( testClass );
      this.config = requireNonNull( config );
      this.webDriver = requireNonNull( webDriver );
      this.name = new TestNameImpl( testClass );
      this.testResultsDir = testResultsDir;
      this.log = new TestLogImpl( this.startTime,
                                  config.logHandlers()
                                        .stream()
                                        .map( factory -> factory.create( name,
                                                                         startTime,
                                                                         testResultsDir ) )
                                        .collect( ImmutableList.toImmutableList() ) );
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
            bind( Clock.class ).toInstance( Clock.systemUTC() );
            bind( testClass ).in( Singleton.class );
            bind( RemoteWebDriver.class ).toInstance( webDriver );
            bind( WebDriver.class ).to( RemoteWebDriver.class );
            bind( TakesScreenshot.class ).to( RemoteWebDriver.class );
            bind( TestLog.class ).toInstance( log );
            bind( JeleniumConfig.class ).toInstance( config );
            bind( ScreenshotSaver.class ).to( ScreenshotSaverImpl.class );
            bind( TestContext.class ).to( TestContextImpl.class );
            bind( HttpUrl.class ).toInstance( config.url() );
            bind( TestName.class ).toInstance( name );
            bind( Path.class ).annotatedWith( TestResultDir.class ).toInstance( testResultsDir );
         }
      } );
      return Guice.createInjector( modules );
   }

   private void finish() {
      final JeleniumTest jt = this.testInstance.get();

      if ( this.result == TestResultState.PASSED ) {
         log.info().msg( "Passed: %s", name ).log();
         try {
            jt.onPass( getContext() );
         } catch ( Throwable ex ) {
            log.error().ex( ex ).log();
         }
      }

      if ( this.result == TestResultState.FAILED ) {
         log.warn().msg( "Failed: %s", name ).log();
         try {
            jt.onFail( getContext() );
            // Take a screenshot at the end on failure.
            getContext().getScreenshotSaver().saveScreenshot( "failed" );
         } catch ( Throwable ex ) {
            log.error().ex( ex ).log();
         }
      }

      try {
         jt.onFinish( getContext(), this.result );
      } catch ( Exception ex ) {
         log.error().ex( ex ).log();
      }

      // Close log files.
      try {
         this.log.close();
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   private TestContext getContext() {
      return injector.orElseThrow( () -> new IllegalStateException( "GUICE injector not created yet" ) )
                     .getInstance( TestContext.class );
   }

   int getTestRetries() {
      final int fromTest = testInstance.get().retries();
      if ( 0 <= fromTest ) {
         return fromTest;
      } else {
         return this.config.testRetries();
      }
   }

   /**
    * Run the test.
    */
   public void run() {
      log.info().msg( "Starting Test %s @ %s", name, this.startTime.toString() ).log();

      try {
         // Web driver must be set before creating injector.
         this.injector = of( createGuiceInjector() );
         this.testInstance = of( injector.get().getInstance( testClass ) );

         final Options manage = this.webDriver.manage();
         manage.timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
         manage.timeouts().pageLoadTimeout( 20, TimeUnit.SECONDS );
         manage.timeouts().setScriptTimeout( 20, TimeUnit.SECONDS );
         testInstance.get().manage( manage );

      } catch ( Exception ex ) {
         if ( !this.injector.isPresent() ) {
            this.result = TestResultState.ERROR;
            log.error().msg( "Failed to create guice injector" ).ex( ex ).log();
         } else if ( !this.testInstance.isPresent() ) {
            this.result = TestResultState.ERROR;
            log.error()
               .msg( "Failed to instantiate test class %s", testClass.getName() )
               .ex( ex )
               .log();
         } else {
            log.error().ex( ex ).log();
            this.result = TestResultState.FAILED;
         }
      }

      log.info()
         .msg( "Using webdriver '%s'", this.webDriver.toString() )
         .newline()
         .msg( "Browser: %s %s",
               webDriver.getCapabilities().getBrowserName(),
               webDriver.getCapabilities().getVersion() )
         .log();

      this.result = this.runTest( this.testInstance.get() );

      this.finish();
   }

   private TestResultState runTest( JeleniumTest test ) {
      try {

         if ( test.skipTest() ) {
            throw new SkipTestException( "Statically skipped" );
         }

         test.onBeforeRun( getContext() );
         test.run( getContext() );
         return TestResultState.PASSED;

      } catch ( SkipTestException ex ) {

         log.info().msg( "Skipping test: %s because %s", name, ex.getMessage() ).log();
         return TestResultState.SKIPPED;

      } catch ( Throwable ex ) {
         log.error().ex( ex ).log();
         return TestResultState.FAILED;
      }
   }

   JeleniumTestResult toResult() {
      return new TestResultImpl( this.name,
                                 this.result,
                                 this.getTestRetries(),
                                 Utils.findWebDriverName( this.webDriver ) );
   }
}
