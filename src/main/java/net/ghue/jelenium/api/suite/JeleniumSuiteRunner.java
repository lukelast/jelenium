package net.ghue.jelenium.api.suite;

import java.util.Collection;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.TestManager;

public interface JeleniumSuiteRunner {

   JeleniumSettings getSettings();

   void runTests( Collection<TestManager> tests ) throws Exception;

   void setSettings( JeleniumSettings settings );
}
