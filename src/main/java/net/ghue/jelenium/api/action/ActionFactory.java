package net.ghue.jelenium.api.action;

import java.util.function.Supplier;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface ActionFactory {

   SimpleAction build( Runnable simpleAction );

   <O> ActionBuilder<O> start( ActionStep<RemoteWebDriver, O> firstAction );

   <O> ActionBuilder<O> start( Supplier<O> firstAction );
}
