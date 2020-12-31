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
import net.ghue.jelenium.impl.Utils;
import net.ghue.jelenium.impl.suite.WebDriverSessionBase;

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
   public void close() {}

   @Override
   public WebDriverSession createWebDriver() {
      return new WebDriverSessionBase( Utils.newInstance( this.driverClass ) );
   }

   @Override
   public void init( JeleniumConfig config ) {

      final String webDriverName = config.browserName().toLowerCase( Locale.ROOT );

      for ( Class<? extends RemoteWebDriver> cls : DRIVERS ) {
         if ( cls.getName().toLowerCase( Locale.ROOT ).contains( webDriverName ) ) {
            driverClass = cls;
         }
      }
   }

}
