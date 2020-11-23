package net.ghue.jelenium.api;

import java.util.List;
import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.suite.WebDriverProvider;

public interface TestManager {

   TestName getName();

   Stream<JeleniumTestResult> getResults();

   JeleniumTestResult run( RemoteWebDriver remoteWebDriver, int attempt );

   List<JeleniumTestResult> runWithRetries( WebDriverProvider driverProvider );

}
