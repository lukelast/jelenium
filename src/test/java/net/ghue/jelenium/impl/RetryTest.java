package net.ghue.jelenium.impl;
import static org.mockito.Mockito.mock;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestResultState;

public class RetryTest {

   static final class Fail1ThenPassTest implements JeleniumTest {

      private final AtomicInteger runCount = new AtomicInteger( 0 );

      @Override
      public int retries() {
         return 1;
      }

      @Override
      public void run( TestContext context ) throws Exception {
         final int currentRun = this.runCount.incrementAndGet();
         if ( currentRun < 2 ) {
            throw new RuntimeException( "Fail" );
         }
      }
   }

   static final class GlobalRetries implements JeleniumTest {

      private final AtomicInteger runCount = new AtomicInteger( 0 );

      @Override
      public void run( TestContext context ) throws Exception {
         final int currentRun = this.runCount.incrementAndGet();
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

   @Test
   public void fail1ThenPass() throws Exception {
      TestManagerImpl manager = new TestManagerImpl( Fail1ThenPassTest.class, new SettingsMock() );

      manager.run( mock( RemoteWebDriver.class ) );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 2 )
                .containsExactly( TestResultState.FAILED, TestResultState.PASSED );
   }

   @Test
   public void noRetries() throws Exception {
      TestManagerImpl manager = new TestManagerImpl( NoRetries.class, new SettingsMock() );

      manager.run( mock( RemoteWebDriver.class ) );

      Assertions.assertThat( manager.getResults().map( JeleniumTestResult::getResult ) )
                .hasSize( 1 )
                .containsExactly( TestResultState.FAILED );
   }

}
