package net.ghue.jelenium.api.action;

import java.time.Duration;

public interface ActionBuilder<I> {

   <O> ActionBuilder<O> add( ActionStep<I, O> actionStep );

   Action<I> build();

   default <O> Action<O> build( ActionStep<I, O> finalAction ) {
      return add( finalAction ).build();
   }

   SimpleAction buildSimple();

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

   Duration getRetryDelay();

   Duration getRetryTimeout();

   /**
    * @param delay How long to pause after a failure before trying again.
    */
   void setRetryDelay( Duration delay );

   /**
    * @param timeout How long to keep retrying the action if it fails.
    */
   void setRetryTimeout( Duration timeout );
}
