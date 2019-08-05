package net.ghue.jelenium.impl.action;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.action.Action;

final class ActionExecutor<R> implements Action<R> {

   private final Supplier<R> action;

   private final Duration retryDelay;

   private final Duration retryTimeout;

   private final TestContext testContext;

   ActionExecutor( ActionBuilderImpl<R> actionBuilder ) {
      this.action = actionBuilder.action;
      this.testContext = actionBuilder.testContext;
      this.retryTimeout = actionBuilder.retryTimeout;
      this.retryDelay = actionBuilder.retryDelay;
   }

   @Override
   public R execute() {
      final Instant start = Instant.now();

      while ( true ) {
         try {
            return this.action.get();
         } catch ( Throwable ex ) {
            if ( Instant.now().minus( retryTimeout ).isAfter( start ) ) {
               throw ex;
            } else {
               // Retry.
               try {
                  Thread.sleep( this.retryDelay.toMillis() );
               } catch ( InterruptedException iex ) {
                  throw new RuntimeException( iex );
               }
               this.testContext.getLog().debug( "Retrying Action" );
            }
         }
      }
   }

}
