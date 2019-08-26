package net.ghue.jelenium.impl;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.TestResultsHandler;

final class ResultsHandlersProvider implements Provider<List<TestResultsHandler>> {

   @SuppressWarnings( "unused" )
   private final JeleniumSettings settings;

   @Inject
   ResultsHandlersProvider( JeleniumSettings settings ) {
      this.settings = settings;
   }

   @Override
   public List<TestResultsHandler> get() {
      // TODO Allow adding/changing handlers from settings or class path.

      return ImmutableList.of( new ResultsHandlerStdOut() );
   }

}
