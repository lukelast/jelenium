package net.ghue.jelenium.api;

import java.util.Collection;

public interface TestResultsHandler {

   void processResults( Collection<JeleniumTestResult> results );
}
