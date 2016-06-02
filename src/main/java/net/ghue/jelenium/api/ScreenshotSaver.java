package net.ghue.jelenium.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import javax.inject.Inject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.base.Ascii;
import net.ghue.jelenium.api.annotation.TestName;
import net.ghue.jelenium.api.annotation.TestResultDir;

/**
 * TODO This should be an interface and IMPL but initially writing as one to save time.
 * 
 * @author Luke Last
 */
public final class ScreenshotSaver {

   @Inject
   private RemoteWebDriver driver;

   @Inject
   private TestLog log;

   @Inject
   @TestResultDir
   private Path resultsDir;

   @Inject
   @TestName
   private String testName;

   /**
    * Save a browser screenshot as PNG to the test results directory.
    * 
    * @param name Some text to identify the screenshot. This will only be part of the full file
    *           name.
    */
   public void saveScreenshot( String name ) {
      final byte[] shotData = driver.getScreenshotAs( OutputType.BYTES );
      final StringBuilder fileName = new StringBuilder( 64 );

      fileName.append( testName )
              .append( '-' )
              .append( Ascii.truncate( name, 50, "" ) )
              .append( '-' )
              .append( Instant.now().toEpochMilli() )
              .append( ".png" );

      final Path shotPath = resultsDir.resolve( fileName.toString() );

      try {
         Files.createDirectories( resultsDir );
         Files.write( shotPath, shotData, StandardOpenOption.CREATE_NEW );
      } catch ( IOException ex ) {
         log.error( "Saving screenshot '" + shotPath + "'", ex );
      }

   }

}
