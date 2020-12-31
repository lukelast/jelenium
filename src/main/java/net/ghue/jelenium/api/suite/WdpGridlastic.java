package net.ghue.jelenium.api.suite;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.impl.suite.WebDriverSessionBase;

public class WdpGridlastic implements WebDriverProvider {

   private JeleniumConfig jeleniumConfig;

   @Override
   public void close() {}

   protected void configChrome( DesiredCapabilities cap ) {
      ChromeOptions options = new ChromeOptions();
      if ( isLinux() ) {
         // On Linux start-maximized does not expand browser window to max screen size. Always set a window size and position.
         options.addArguments( Arrays.asList( "--window-position=0,0" ) );
         // starting with Chrome version 83, use width of 1840 instead of 1920 to capture the entire webpage on video recording.
         options.addArguments( Arrays.asList( "--window-size=1840,1080" ) );
      } else {
         options.addArguments( Arrays.asList( "--start-maximized" ) );
      }
      cap.setCapability( ChromeOptions.CAPABILITY, options );
   }

   protected void configFirefox( DesiredCapabilities cap ) {
      FirefoxOptions ffOptions = new FirefoxOptions();

      // Required to specify firefox binary location on Gridlastic grid nodes starting from selenium version 3.5.3+
      // see firefox documentation https://www.gridlastic.com/test-environments.html#firefox_testing           
      if ( isLinux() ) {
         ffOptions.setBinary( "/home/ubuntu/Downloads/firefox" +
                              jeleniumConfig.browserVersion() +
                              "/firefox" );
      } else {
         ffOptions.setBinary( "C:\\Program Files (x86)\\Mozilla Firefox\\firefox" +
                              jeleniumConfig.browserVersion() +
                              "\\firefox.exe" );
      }
      cap.setCapability( "moz:firefoxOptions", ffOptions );
   }

   /**
    * @see "https://github.com/Gridlastic/selenium-grid-java-example"
    */
   protected DesiredCapabilities createDesiredCapabilities() {
      final DesiredCapabilities cap = new DesiredCapabilities();

      cap.setCapability( "platform", jeleniumConfig.browserPlatform() );

      if ( !isLinux() ) {
         cap.setCapability( "platformName", "windows" );
      }

      cap.setBrowserName( jeleniumConfig.browserName() );
      cap.setVersion( jeleniumConfig.browserVersion() );

      if ( jeleniumConfig.browserVideo() ) {
         // NOTE: "True" is a case sensitive string, not boolean.
         cap.setCapability( "video", "True" );
      }

      if ( isChrome() ) {
         configChrome( cap );
      }
      if ( isFirefox() ) {
         configFirefox( cap );
      }

      return cap;
   }

   @Override
   public WebDriverSession createWebDriver() {

      StringBuilder urlBuilder = new StringBuilder();
      urlBuilder.append( "https://" )
                .append( jeleniumConfig.gridlasticUsername() )
                .append( ":" )
                .append( jeleniumConfig.gridlasticAccessKey() )
                .append( "@" )
                .append( jeleniumConfig.gridlasticHubSubdomain() )
                .append( ".gridlastic.com/wd/hub" );

      final URL url;
      try {
         url = new URL( urlBuilder.toString() );
      } catch ( MalformedURLException ex ) {
         throw new UncheckedIOException( ex );
      }

      RemoteWebDriver webDriver = new RemoteWebDriver( url, createDesiredCapabilities() );

      // On LINUX/FIREFOX the "driver.manage().window().maximize()" option does not expand browser window to max screen size.
      // Always set a window size.
      if ( isLinux() && isFirefox() ) {
         webDriver.manage().window().setSize( new Dimension( 1920, 1080 ) );
      }

      return new WebDriverSessionBase( webDriver );
   }

   protected String getBrowser() {
      return this.jeleniumConfig.browserName();
   }

   @Override
   public void init( JeleniumConfig config ) {
      this.jeleniumConfig = config;
   }

   protected boolean isChrome() {
      return "chrome".equalsIgnoreCase( jeleniumConfig.browserName() );
   }

   protected boolean isFirefox() {
      return "firefox".equalsIgnoreCase( jeleniumConfig.browserName() );
   }

   protected boolean isLinux() {
      return "linux".equalsIgnoreCase( jeleniumConfig.browserPlatform() );
   }

}
