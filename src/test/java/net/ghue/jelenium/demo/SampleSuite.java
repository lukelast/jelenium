package net.ghue.jelenium.demo;

import java.util.Collection;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.suite.TestManager;

public final class SampleSuite implements JeleniumSuiteRunner {

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig config ) throws Exception {}
}
