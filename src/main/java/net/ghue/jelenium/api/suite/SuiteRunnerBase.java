package net.ghue.jelenium.api.suite;

import javax.annotation.Nonnull;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumSettings;

public abstract class SuiteRunnerBase implements JeleniumSuiteRunner {

   private JeleniumSettings settings;

   @Nonnull
   protected abstract RemoteWebDriver createWebDriver();

   @Override
   public JeleniumSettings getSettings() {
      return this.settings;
   }

   @Override
   public void setSettings( JeleniumSettings settings ) {
      this.settings = settings;
   }
}
