package net.ghue.jelenium.api.suite;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;

public class WorkerChrome implements Worker {

   @Override
   public void init() {}

   @Override
   public void finish() {}

   @Override
   public JeleniumTestResult run( TestManager tm, int attempt ) {

      final RemoteWebDriver driver = new ChromeDriver();
      try {
         return tm.run( driver, attempt );
      } finally {
         driver.quit();
      }
   }

}
