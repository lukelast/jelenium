package net.ghue.jelenium.impl.action;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.action.Action;
import net.ghue.jelenium.api.action.ActionBuilder;
import net.ghue.jelenium.api.action.ActionStep;
import net.ghue.jelenium.api.action.RetryableAction;
import net.ghue.jelenium.api.action.SimpleAction;

final class ActionBuilderImpl<I> implements ActionBuilder<I> {

   Supplier<I> action;

   Duration retryDelay = Duration.ZERO;

   Duration retryTimeout = Duration.ZERO;

   final TestContext testContext;

   ActionBuilderImpl( Supplier<I> action, TestContext testContext ) {
      this.action = action;
      this.testContext = testContext;

      // TODO Pull default retry settings from settings.
   }

   @Override
   public <O> ActionBuilder<O> add( ActionStep<I, O> actionStep ) {
      final Supplier<I> originalAction = this.action;
      final Supplier<O> nextAction = () -> actionStep.execute( originalAction.get() );
      @SuppressWarnings( "unchecked" )
      final Supplier<I> castAction = (Supplier<I>) nextAction;
      this.action = castAction;
      @SuppressWarnings( "unchecked" )
      final ActionBuilder<O> castBuilder = (ActionBuilder<O>) this;
      return castBuilder;
   }

   @Override
   public Action<I> build() {
      return new ActionExecutor<>( () -> action.get(),
                                   this.retryDelay,
                                   this.retryTimeout,
                                   this.testContext );
   }

   @Override
   public SimpleAction buildSimple() {
      return new SimpleAction() {

         @Override
         public void execute() {
            build().execute();
         }
      };
   }

   @Override
   public Duration getRetryDelay() {
      return this.retryDelay;
   }

   @Override
   public Duration getRetryTimeout() {
      return this.retryTimeout;
   }

   @Override
   @Nonnull
   public ActionBuilder<I> withRetry( @Nullable RetryableAction annotation ) {
      if ( annotation != null ) {
         this.retryDelay = Duration.ofMillis( (long) ( annotation.retryDelaySec() * 1_000 ) );
         this.retryTimeout = Duration.ofMillis( (long) ( annotation.retryTimeoutSec() * 1_000 ) );
      }
      return this;
   }

   @Override
   public ActionBuilderImpl<I> withRetryDelay( @Nullable Duration delay ) {
      this.retryDelay = Objects.requireNonNull( delay );
      return this;
   }

   @Override
   public ActionBuilderImpl<I> withRetryTimeout( @Nullable Duration timeout ) {
      this.retryTimeout = Objects.requireNonNull( timeout );
      return this;
   }

}
