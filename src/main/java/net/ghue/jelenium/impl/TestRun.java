package net.ghue.jelenium.impl;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.*;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.openqa.selenium.WebDriver;
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

final class TestRun implements Closeable {

   private Optional<Injector> injector = empty();

   private final TestLog log = new StandardOutLog();

   private final String name;

   private TestResult result = TestResult.NOT_RUN;

   private final JeleniumSettings settings;

   private final Class<? extends JeleniumTest> testClass;

   private Optional<JeleniumTest> theTest = empty();

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

   @Override
   public void close() throws IOException {
      try {
         if ( webDriver.isPresent() ) {
            webDriverManager.giveBack( webDriver.get() );
         }
      } catch ( Throwable ex ) {
         log.error( "", ex );
      }
   }

   private TestContext getContext() {
      return injector.orElseThrow( () -> new IllegalStateException( "GUICE injector not created yet" ) )
                     .getInstance( TestContext.class );
   }

   public TestResult getResult() {
      return requireNonNull( this.result );
   }

   private void init() {

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

      this.theTest = of( requireNonNull( injector.get().getInstance( testClass ),
                                         testClass.getName() + " could not be instantiated." ) );

      this.initWebDriver( injector.get(), theTest.get() );
   }

   private void initWebDriver( Injector injector, JeleniumTest test ) {
      WebDriver driver = injector.getInstance( WebDriver.class );

      driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
      driver.manage().timeouts().pageLoadTimeout( 20, TimeUnit.SECONDS );
      driver.manage().timeouts().setScriptTimeout( 20, TimeUnit.SECONDS );

      test.manage( driver.manage() );
   }

   /**
    * @return {@code true} if the test passed.
    */
   boolean passed() {
      return this.result == TestResult.PASSED;
   }

   /**
    * Run the test.
    */
   void run() {
      try {

         if ( !settings.getFilter().isEmpty() &&
              !name.toLowerCase().contains( settings.getFilter() ) ) {
            log.info( "Skipping test: %s", name );
            this.result = TestResult.SKIPPED;
            return;
         }

         log.info( "Starting Test: %s", name );
         this.init();
         if ( theTest.get().skipTest() ) {
            return;
         }
         theTest.get().onBeforeRun( getContext() );
         theTest.get().run( getContext() );
         this.result = TestResult.PASSED;

      } catch ( Throwable ex ) {
         log.error( "", ex );
         this.result = TestResult.FAILED;
      }

      if ( !theTest.isPresent() ) {
         throw new IllegalStateException( "Failed to instantiate " + testClass );
      }

      if ( !injector.isPresent() ) {
         throw new IllegalStateException( "Failed to create guice injector" );
      }

      if ( this.result == TestResult.PASSED ) {
         log.info( "Passed: %s", name );
         try {
            theTest.get().onPass( getContext() );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      } else {
         log.warn( "Failed: %s", name );
         try {
            theTest.get().onFail( getContext() );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      }

      try {
         theTest.get().onFinish( getContext() );
      } catch ( Throwable ex ) {
         log.error( "", ex );
      }
   }

}
