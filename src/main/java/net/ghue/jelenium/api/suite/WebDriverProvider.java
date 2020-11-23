package net.ghue.jelenium.api.suite;

import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;

public interface WebDriverProvider {

   void finished( JeleniumTestResult result, RemoteWebDriver driver );

   RemoteWebDriver get();

}
