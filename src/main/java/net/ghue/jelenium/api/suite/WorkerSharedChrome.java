package net.ghue.jelenium.api.suite;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public class WorkerSharedChrome implements Worker {

   private RemoteWebDriver driver;

   @Override
   public void init() {
      this.driver = new ChromeDriver();
   }

   @Override
   public void finish() {
      this.driver.quit();
   }

   @Override
   public JeleniumTestResult run( TestManager tm, int attempt ) {
      return tm.run( this.driver, attempt );
   }

}
