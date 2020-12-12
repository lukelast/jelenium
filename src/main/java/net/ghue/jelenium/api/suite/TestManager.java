package net.ghue.jelenium.api.suite;

import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.test.JeleniumTestResult;
import net.ghue.jelenium.api.test.TestName;

public interface TestManager {

   TestName getName();

   Stream<JeleniumTestResult> getResults();

   JeleniumTestResult run( RemoteWebDriver remoteWebDriver, int attempt );

}
