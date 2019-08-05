package net.ghue.jelenium.impl.action;

import java.time.Duration;
import java.util.function.Supplier;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.action.Action;
import net.ghue.jelenium.api.action.ActionBuilder;
import net.ghue.jelenium.api.action.ActionStep;
import net.ghue.jelenium.api.action.SimpleAction;

final class ActionBuilderImpl<I> implements ActionBuilder<I> {

   Supplier<I> action;

   Duration retryDelay = Duration.ofSeconds( 1 );

   Duration retryTimeout = Duration.ofSeconds( 10 );

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
      return new ActionExecutor<>( this );
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
   public void setRetryDelay( Duration delay ) {
      this.retryDelay = delay;
   }

   @Override
   public void setRetryTimeout( Duration timeout ) {
      this.retryTimeout = timeout;
   }

}
