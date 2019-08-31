package net.ghue.jelenium.impl;
import static org.mockito.Mockito.mock;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestResultState;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.WebDriverProvider;
import okhttp3.HttpUrl;

public class RetryTest {

   static final class Fail1ThenPassTest implements JeleniumTest {

      private static final AtomicInteger RUN_COUNT = new AtomicInteger( 0 );

      @Override
      public int retries() {
         return 1;
      }

      @Override
      public void run( TestContext context ) throws Exception {
         final int currentRun = RUN_COUNT.incrementAndGet();
         if ( currentRun < 2 ) {
            throw new RuntimeException( "Fail" );
         }
      }
   }

   static final class GlobalRetries implements JeleniumTest {

      static final AtomicInteger RUN_COUNT = new AtomicInteger( 0 );

      @Override
      public void run( TestContext context ) throws Exception {
         final int currentRun = RUN_COUNT.incrementAndGet();
         if ( currentRun < 4 ) {
            throw new RuntimeException( "Fail" );
         }
      }
   }

   static final class NoRetries implements JeleniumTest {

      @Override
      public int retries() {
         return 0;
      }

      @Override
      public void run( TestContext context ) throws Exception {
         throw new RuntimeException( "Fail" );
      }
   }

   private WebDriverProvider create() {
      return new WebDriverProvider() {

         @Override
         public void finished( JeleniumTestResult result, RemoteWebDriver driver ) {}

         @Override
         public RemoteWebDriver get() {
            RemoteWebDriver driver = mock( RemoteWebDriver.class, Mockito.RETURNS_MOCKS );
            return driver;
         }

      };
   }

   @Test
   public void fail1ThenPass() throws Exception {
      TestManagerImpl manager = new TestManagerImpl( Fail1ThenPassTest.class, mockConfig() );

      manager.runWithRetries( create() );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 2 )
                .containsExactly( TestResultState.FAILED_RETRIED, TestResultState.PASSED );
   }

   private JeleniumConfig mockConfig() {
      JeleniumConfig config = mock( JeleniumConfig.class );
      Mockito.when( config.results() ).thenReturn( Files.temporaryFolder().toPath() );
      Mockito.when( config.suite() ).thenReturn( null );
      Mockito.when( config.logHandlers() ).thenReturn( Collections.emptyList() );
      Mockito.when( config.url() ).thenReturn( HttpUrl.get( "http://localhost" ) );
      return config;
   }

   @Test
   public void noRetries() throws Exception {

      TestManagerImpl manager = new TestManagerImpl( NoRetries.class, mockConfig() );

      manager.runWithRetries( create() );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 1 )
                .containsExactly( TestResultState.FAILED );
   }

   @Test
   public void testGlobalRetries2() throws Exception {
      GlobalRetries.RUN_COUNT.set( 0 );
      JeleniumConfig config = mockConfig();
      Mockito.when( config.testRetries() ).thenReturn( 2 );

      TestManagerImpl manager = new TestManagerImpl( GlobalRetries.class, config );

      manager.runWithRetries( create() );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 3 )
                .containsExactly( TestResultState.FAILED_RETRIED,
                                  TestResultState.FAILED_RETRIED,
                                  TestResultState.FAILED );
   }

   @Test
   public void testGlobalRetries4() throws Exception {
      GlobalRetries.RUN_COUNT.set( 0 );
      JeleniumConfig config = mockConfig();
      Mockito.when( config.testRetries() ).thenReturn( 99 );
      TestManagerImpl manager = new TestManagerImpl( GlobalRetries.class, config );

      manager.runWithRetries( create() );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 4 )
                .containsExactly( TestResultState.FAILED_RETRIED,
                                  TestResultState.FAILED_RETRIED,
                                  TestResultState.FAILED_RETRIED,
                                  TestResultState.PASSED );
   }

}
