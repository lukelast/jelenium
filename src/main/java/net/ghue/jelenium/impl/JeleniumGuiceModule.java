package net.ghue.jelenium.impl;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.TestResultsHandler;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

public final class JeleniumGuiceModule extends AbstractModule {

   private final String[] args;

   public JeleniumGuiceModule( String[] args ) {
      this.args = Objects.requireNonNull( args );
   }

   @Provides
   String[] getArgs() {
      return this.args;
   }

   @Override
   protected void configure() {
      bind( JeleniumSettings.class ).toProvider( SettingsProvider.class ).in( Singleton.class );
      bind( JeleniumSuiteRunner.class ).toProvider( SuiteRunnerProvider.class );
      bind( new TypeLiteral<List<TestResultsHandler>>() {} ).toProvider( ResultsHandlersProvider.class );
      bind( PrintStream.class ).toInstance( System.out );

   }

}
