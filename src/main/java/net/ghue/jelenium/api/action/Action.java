package net.ghue.jelenium.api.action;

@FunctionalInterface
public interface Action<R> {

   R execute();
}
