package net.ghue.jelenium.api;

import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface TestManager {

   JeleniumTestResult run( RemoteWebDriver remoteWebDriver );

   Stream<JeleniumTestResult> getResults();

   TestName getName();

}
