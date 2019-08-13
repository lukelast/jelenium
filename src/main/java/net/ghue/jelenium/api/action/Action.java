package net.ghue.jelenium.api.action;

@FunctionalInterface
public interface Action<R> extends ThrowableAction<R> {

   @Override
   R execute();
}
