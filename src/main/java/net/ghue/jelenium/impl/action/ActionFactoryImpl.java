package net.ghue.jelenium.impl.action;

import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.action.ActionBuilder;
import net.ghue.jelenium.api.action.ActionFactory;
import net.ghue.jelenium.api.action.ActionStep;
import net.ghue.jelenium.api.action.SimpleAction;
import net.ghue.jelenium.api.action.ThrowableAction;
import net.ghue.jelenium.api.test.TestContext;

public final class ActionFactoryImpl implements ActionFactory {

   private final TestContext testContext;

   public ActionFactoryImpl( TestContext testContext ) {
      this.testContext = testContext;
   }

   @Override
   public SimpleAction build( Runnable simpleAction ) {
      return start( () -> {
         simpleAction.run();
         return null;
      } ).buildSimple();
   }

   @Override
   public <O> ActionBuilder<O> start( ActionStep<RemoteWebDriver, O> firstAction ) {
      return start( () -> firstAction.execute( this.testContext.getWebDriver() ) );
   }

   @Override
   public <O> ActionBuilder<O> start( ThrowableAction<O> firstAction ) {
      return new ActionBuilderImpl<>( firstAction.toSupplier(), this.testContext );
   }

}
