package net.ghue.jelenium.api;

import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface TestManager {

   TestName getName();

   Stream<JeleniumTestResult> getResults();

   JeleniumTestResult run( RemoteWebDriver remoteWebDriver );

}
