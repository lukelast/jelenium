package net.ghue.jelenium.impl.action;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.ghue.jelenium.api.action.Action;
import net.ghue.jelenium.api.action.ActionBuilder;
import net.ghue.jelenium.api.action.ActionStep;
import net.ghue.jelenium.api.action.SimpleAction;

final class ActionBuilderImpl<I> implements ActionBuilder<I> {

   private Supplier<I> action;

   ActionBuilderImpl( Supplier<I> action ) {
      this.action = action;
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
      return new ActionExecutor<>( this.action );
   }

   @Override
   public SimpleAction buildSimple() {
      return new SimpleActionExecutor( () -> this.action.get() );
   }

   @Override
   public SimpleAction buildSimple( Consumer<I> finalAction ) {
      return new SimpleActionExecutor( () -> finalAction.accept( this.action.get() ) );
   }

}
