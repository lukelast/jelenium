package net.ghue.jelenium.demo.guice;

import com.google.inject.AbstractModule;

public final class MyGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind( MyService.class ).to( MyServiceImpl.class );
   }
}
