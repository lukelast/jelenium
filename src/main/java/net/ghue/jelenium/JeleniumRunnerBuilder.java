package net.ghue.jelenium;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.ghue.jelenium.impl.test.GuiceModule;
import net.ghue.jelenium.impl.test.JeleniumRunner;

public final class JeleniumRunnerBuilder {

   @Nullable
   private String[] args;

   public JeleniumRunnerBuilder withArgs( String[] newArgs ) {
      this.args = newArgs;
      return this;
   }

   @Nonnull
   public String[] getArgs() {
      return ( args != null ) ? args : new String[0];
   }

   public JeleniumRunner build() {
      final Injector injector = Guice.createInjector( new GuiceModule( getArgs() ) );
      final JeleniumRunner jelenium = injector.getInstance( JeleniumRunner.class );
      return jelenium;
   }
}
