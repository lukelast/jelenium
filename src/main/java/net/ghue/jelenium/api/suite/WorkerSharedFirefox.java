package net.ghue.jelenium.api.suite;

import java.util.Objects;
import javax.annotation.Nullable;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public class WorkerSharedFirefox implements Worker {

   @Nullable
   private RemoteWebDriver driver;

   @Override
   public void init() {
      this.driver = new FirefoxDriver();
   }

   @Override
   public void finish() {
      Objects.requireNonNull( this.driver );
      this.driver.quit();
   }

   @Override
   public JeleniumTestResult run( TestManager tm, int attempt ) {
      Objects.requireNonNull( this.driver );
      return tm.run( this.driver, attempt );
   }

}
