package net.ghue.jelenium.impl.action;

import java.time.Duration;
import java.time.Instant;
import com.google.common.base.Throwables;
import net.ghue.jelenium.api.action.Action;
import net.ghue.jelenium.api.action.ThrowableAction;
import net.ghue.jelenium.api.test.TestContext;

final class ActionExecutor<R> implements Action<R> {

   private static Duration findDuration( Duration local, Duration global ) {
      if ( local.isZero() || local.isNegative() ) {
         return global;
      } else {
         return local;
      }
   }

   private final ThrowableAction<R> action;

   private final Duration retryDelay;

   private final Duration retryTimeout;

   private final TestContext testContext;

   public ActionExecutor( ThrowableAction<R> action, Duration retryDelay, Duration retryTimeout,
                          TestContext testContext ) {
      this.action = action;
      this.testContext = testContext;
      this.retryDelay = findDuration( retryDelay, testContext.getConfig().retryDelay() );
      this.retryTimeout = findDuration( retryTimeout, testContext.getConfig().retryTimeout() );
   }

   @Override
   public R execute() {
      final Instant start = Instant.now();

      while ( true ) {
         try {
            return this.action.execute();
         } catch ( Throwable ex ) {
            if ( Instant.now().minus( retryTimeout ).isAfter( start ) ) {
               Throwables.throwIfUnchecked( ex );
               throw new RuntimeException( ex );
            } else {
               // Retry.
               try {
                  Thread.sleep( this.retryDelay.toMillis() );
               } catch ( InterruptedException iex ) {
                  throw new RuntimeException( iex );
               }
               this.testContext.log().debug().msg( "Retrying Action" ).log();
            }
         }
      }
   }

}
