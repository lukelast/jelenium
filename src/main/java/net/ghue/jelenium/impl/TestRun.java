package net.ghue.jelenium.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.openqa.selenium.WebDriver;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.ghue.jelenium.api.GuiceModule;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.SeleniumTest;
import net.ghue.jelenium.api.TestArgs;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestLog;

final class TestRun implements Closeable {

   private Injector injector;

   private final TestLog log = new StandardOutLog();

   private final TestArgs testArgs;

   private final Class<? extends SeleniumTest> testClass;

   private boolean testPassed = true;

   private SeleniumTest theTest;

   private final WebDriver webDriver;

   /**
    * <p>
    * Constructor for TestRun.
    * </p>
    *
    * @param testClass a {@link java.lang.Class} object.
    * @param webDriver a {@link org.openqa.selenium.WebDriver} object.
    * @param testArgs a {@link net.ghue.jelenium.api.TestArgs} object.
    */
   public TestRun( Class<? extends SeleniumTest> testClass, WebDriver webDriver,
                   TestArgs testArgs ) {
      this.testClass = testClass;
      this.webDriver = webDriver;
      this.testArgs = testArgs;
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
            bind( TestLog.class ).toInstance( log );
            bind( TestArgs.class ).toInstance( testArgs );
            bind( TestContext.class ).to( TestContextImpl.class );
            bind( HttpUrl.class ).toInstance( testArgs.getUrl() );
         }
      } );

      injector = Guice.createInjector( modules );

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
      return this.testPassed;
   }

   /**
    * Run the test.
    */
   void run() {
      try {
         log.info( "Starting Test: %s", testClass.getSimpleName() );
         this.init();
         theTest.run( injector.getInstance( TestContext.class ) );

      } catch ( Throwable ex ) {
         log.error( "", ex );
         this.testPassed = false;
      }

      if ( theTest == null ) {
         throw new IllegalStateException( "Failed to instantiate " + testClass );
      }

      if ( this.testPassed ) {
         log.info( "Passed: %s", theTest.getClass().getSimpleName() );
         try {
            theTest.onPass( injector.getInstance( TestContext.class ) );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      } else {
         log.warn( "Failed: %s", theTest.getClass().getSimpleName() );
         try {
            theTest.onFail( injector.getInstance( TestContext.class ) );
         } catch ( Throwable ex ) {
            log.error( "", ex );
         }
      }

      try {
         theTest.onFinish( injector.getInstance( TestContext.class ) );
      } catch ( Throwable ex ) {
         log.error( "", ex );
      }
   }

}
