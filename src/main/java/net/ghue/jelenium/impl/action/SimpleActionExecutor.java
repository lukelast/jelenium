package net.ghue.jelenium.impl.action;

import net.ghue.jelenium.api.action.SimpleAction;

final class SimpleActionExecutor implements SimpleAction {

   private final Runnable simpleAction;

   public SimpleActionExecutor( Runnable simpleAction ) {
      this.simpleAction = simpleAction;
   }

   @Override
   public void execute() {
      //TODO retry
      this.simpleAction.run();
   }
}
