package net.ghue.jelenium.api.suite;

import javax.annotation.Nonnull;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.config.JeleniumConfig;

public abstract class SuiteRunnerBase implements JeleniumSuiteRunner {

   private JeleniumConfig config;

   @Nonnull
   protected abstract RemoteWebDriver createWebDriver();

   @Override
   public JeleniumConfig getConfig() {
      return this.config;
   }

   @Override
   public void setConfig( JeleniumConfig config ) {
      this.config = config;
   }
}
