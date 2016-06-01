package net.ghue.jelenium.impl;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.ghue.jelenium.api.GuiceModule;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.SeleniumTest;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestLog;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultDir;

final class TestRun implements Closeable {

   private Injector injector;

   private final TestLog log = new StandardOutLog();

   private final JeleniumSettings settings;

   private final Class<? extends SeleniumTest> testClass;

   private TestResult result = TestResult.NOT_RUN;

   private SeleniumTest theTest;

   private final RemoteWebDriver webDriver;

   private final String name;

   /**
    * <p>
    * Constructor for TestRun.
    * </p>
    *
    * @param testClass a {@link java.lang.Class} object.
    * @param webDriver a {@link org.openqa.selenium.WebDriver} object.
    * @param settings a {@link net.ghue.jelenium.api.JeleniumSettings} object.
    */
   public TestRun( Class<? extends SeleniumTest> testClass, RemoteWebDriver webDriver,
                   JeleniumSettings settings ) {
      this.testClass = testClass;
      this.webDriver = webDriver;
      this.settings = settings;
      this.name = testClass.getSimpleName();
   }

   @Override
   public void close() throws IOException {
      try {
         webDriver.quit();
      } catch ( Throwable ex ) {
         log.error( "", ex );
      }
   }

   private void init() {

      List<Module> modules = new ArrayList<>();

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
            bind( testClass ).in( Singleton.class );
            bind( WebDriver.class ).toInstance( webDriver );
            bind( RemoteWebDriver.class ).toInstance( webDriver );
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

      this.injector = Guice.createInjector( modules );

      this.theTest = Objects.requireNonNull( injector.getInstance( testClass ),
                                             testClass.getName() + " could not be instantiated." );

      this.initWebDriver();
   }

   private void initWebDriver() {
      WebDriver driver = injector.getInstance( WebDriver.class );

      driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
      driver.manage().timeouts().pageLoadTimeout( 20, TimeUnit.SECONDS );
      driver.manage().timeouts().setScriptTimeout( 20, TimeUnit.SECONDS );

      theTest.manage( driver.manage() );
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
         log.info( "Starting Test: %s", name );
         this.init();
         final TestContext context = injector.getInstance( TestContext.class );
         if ( theTest.skipTest() ) {
            return;
         }
         theTest.onBeforeRun( context );
         theTest.run( context );
         this.result = TestResult.PASSED;

      } catch ( Throwable ex ) {
         log.error( "", ex );
         this.result = TestResult.FAILED;
      }

      if ( theTest == null ) {
         throw new IllegalStateException( "Failed to instantiate " + testClass );
      }

      if ( injector == null ) {
         throw new IllegalStateException( "Failed to create guice injector" );
      }

      final TestContext context = injector.getInstance( TestContext.class );

      if ( this.result == TestResult.PASSED ) {
         log.info( "Passed: %s", name );
         try {
            theTest.onPass( context );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      } else {
         log.warn( "Failed: %s", name );
         try {
            theTest.onFail( context );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      }

      try {
         theTest.onFinish( context );
      } catch ( Throwable ex ) {
         log.error( "", ex );
      }
   }

}
