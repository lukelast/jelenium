package net.ghue.jelenium.api.suite;

import java.util.Collection;
import net.ghue.jelenium.api.config.JeleniumConfig;

public interface JeleniumSuiteRunner {

   void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception;
}
