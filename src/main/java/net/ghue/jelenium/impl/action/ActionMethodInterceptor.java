package net.ghue.jelenium.impl.action;

import javax.annotation.Nullable;
import javax.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import net.ghue.jelenium.api.Page;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.action.RetryableAction;

public final class ActionMethodInterceptor implements MethodInterceptor {

   private final Provider<TestContext> testContextProvider;

   public ActionMethodInterceptor( Provider<TestContext> testContextProvider ) {
      this.testContextProvider = testContextProvider;
   }

   @Override
   public Object invoke( @Nullable MethodInvocation invocation ) throws Throwable {
      if ( invocation == null ) {
         return null;
      }
      if ( invocation.getThis() instanceof Page ) {
         @SuppressWarnings( "unused" )
         final Page page = (Page) invocation.getThis();

         final RetryableAction annotation =
               invocation.getMethod().getAnnotation( RetryableAction.class );

         if ( annotation == null ) {
            throw new IllegalStateException( "Method must be annotated with " +
                                             RetryableAction.class.getSimpleName() );
         }
         return testContextProvider.get()
                                   .action()
                                   .start( invocation::proceed )
                                   .withRetry( annotation )
                                   .execute();
      } else {
         return invocation.proceed();
      }
   }
}
