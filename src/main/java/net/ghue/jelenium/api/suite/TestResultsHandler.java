package net.ghue.jelenium.api.suite;

import java.util.Collection;
import net.ghue.jelenium.api.test.JeleniumTestResult;

public interface TestResultsHandler {

   void processResults( Collection<JeleniumTestResult> results );
}
