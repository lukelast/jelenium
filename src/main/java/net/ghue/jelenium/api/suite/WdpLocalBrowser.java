package net.ghue.jelenium.api.suite;

import java.util.List;
import java.util.Locale;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import com.google.common.collect.ImmutableList;
import net.ghue.jelenium.api.config.JeleniumConfig;

public class WdpLocalBrowser implements WebDriverProvider {

   private static List<Class<? extends RemoteWebDriver>> DRIVERS =
         ImmutableList.of( ChromeDriver.class,
                           FirefoxDriver.class,
                           EdgeDriver.class,
                           SafariDriver.class,
                           OperaDriver.class,
                           InternetExplorerDriver.class );

   private Class<? extends RemoteWebDriver> driverClass = ChromeDriver.class;

   @Override
   public void init( JeleniumConfig config ) {

      final String webDriverName = config.suiteBrowser().toLowerCase( Locale.ROOT );

      for ( Class<? extends RemoteWebDriver> cls : DRIVERS ) {
         if ( cls.getName().toLowerCase( Locale.ROOT ).contains( webDriverName ) ) {
            driverClass = cls;
         }
      }
   }

   @Override
   public void close() {}

   @Override
   public RemoteWebDriver createWebDriver() {
      try {
         return this.driverClass.newInstance();
      } catch ( InstantiationException | IllegalAccessException ex ) {
         throw new RuntimeException( ex );
      }
   }

}
