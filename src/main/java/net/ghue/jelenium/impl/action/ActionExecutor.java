package net.ghue.jelenium.impl.action;

import java.util.function.Supplier;
import net.ghue.jelenium.api.action.Action;

final class ActionExecutor<R> implements Action<R> {

   private final Supplier<R> action;

   ActionExecutor( Supplier<R> action ) {
      this.action = action;
   }

   @Override
   public R execute() {
      //TODO retry.
      return this.action.get();
   }

}
