package net.ghue.jelenium.api.action;

@FunctionalInterface
public interface ActionStep<I, O> {

   O execute( I input );
}
