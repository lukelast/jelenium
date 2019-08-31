package net.ghue.jelenium.api.suite;

import java.util.Collection;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;

public interface JeleniumSuiteRunner {

   JeleniumConfig getConfig();

   void runTests( Collection<TestManager> tests ) throws Exception;

   void setConfig( JeleniumConfig config );
}
