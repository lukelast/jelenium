package net.ghue.jelenium.api.action;

import org.openqa.selenium.remote.RemoteWebDriver;

public interface ActionFactory {

   SimpleAction build( Runnable simpleAction );

   <O> ActionBuilder<O> start( ActionStep<RemoteWebDriver, O> firstAction );

   <O> ActionBuilder<O> start( ThrowableAction<O> firstAction );
}
