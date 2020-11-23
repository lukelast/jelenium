package net.ghue.jelenium.api.suite;

import javax.annotation.Nonnull;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class SuiteRunnerBase implements JeleniumSuiteRunner {

   @Nonnull
   protected abstract RemoteWebDriver createWebDriver();

   @Nonnull
   protected abstract WebDriverProvider createProvider();

}
