package net.ghue.jelenium.api.suite;

import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public interface Worker {

   void init();

   void finish();

   JeleniumTestResult run( TestManager tm, int attempt );
}
