package net.ghue.jelenium.api.action;

import java.util.function.Consumer;

public interface ActionBuilder<I> {

   <O> ActionBuilder<O> add( ActionStep<I, O> actionStep );

   Action<I> build();

   default <O> Action<O> build( ActionStep<I, O> finalAction ) {
      return add( finalAction ).build();
   }

   SimpleAction buildSimple();

   SimpleAction buildSimple( Consumer<I> finalAction );

   /**
    * Build and execute.
    * 
    * @return The final result of building and executing the action.
    */
   default I execute() {
      return build().execute();
   }

   default void executeSimple() {
      buildSimple().execute();
   }
}
