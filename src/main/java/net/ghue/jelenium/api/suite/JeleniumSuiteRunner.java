package net.ghue.jelenium.api.suite;

import java.util.Collection;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTestRun;

public interface JeleniumSuiteRunner {

   JeleniumSettings getSettings();

   void processResults( Collection<? extends JeleniumTestRun> tests ) throws Exception;

   void runTests( Collection<? extends JeleniumTestRun> tests ) throws Exception;

   void setSettings( JeleniumSettings settings );

}
