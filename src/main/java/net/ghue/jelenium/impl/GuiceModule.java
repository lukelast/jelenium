package net.ghue.jelenium.impl;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;
import javax.inject.Singleton;
import org.aeonbits.owner.Factory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.impl.config.ConfigFactoryProvider;
import net.ghue.jelenium.impl.config.ConfigProvider;
import net.ghue.jelenium.impl.config.ConfigUpdaterProvider;

public final class GuiceModule extends AbstractModule {

   private final String[] args;

   public GuiceModule( String[] args ) {
      this.args = Objects.requireNonNull( args );
   }

   @Override
   protected void configure() {
      bind( JeleniumConfig.class ).toProvider( ConfigProvider.class ).in( Singleton.class );
      bind( Factory.class ).toProvider( ConfigFactoryProvider.class );
      bind( JeleniumSuiteRunner.class ).toProvider( SuiteRunnerProvider.class );
      bind( new TypeLiteral<Collection<JeleniumConfigUpdater>>() {} ).toProvider( ConfigUpdaterProvider.class );
      bind( PrintStream.class ).toInstance( System.out );

   }

   @Provides
   String[] getArgs() {
      return this.args;
   }

}
