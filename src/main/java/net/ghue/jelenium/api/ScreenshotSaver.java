package net.ghue.jelenium.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import javax.inject.Inject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.google.common.base.Strings;
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
    * Make sure input text is valid for file names.
    * 
    * @param output Filtered chars are appended here.
    * @param input Raw input text.
    * @param limit Maximum number of characters to append.
    */
   private void appendFilteredText( StringBuilder output, String input, int limit ) {
      int charsAdded = 0;
      for ( int index = 0; index < input.length(); index++ ) {
         final char ch = input.charAt( index );
         if ( Character.isLetterOrDigit( ch ) || ch == '-' || ch == '_' ) {
            output.append( ch );
            charsAdded++;
            if ( limit < charsAdded ) {
               break;
            }
         }
      }
   }

   private String createFilename( String name, String elementName ) {
      final StringBuilder filenameBuilder = new StringBuilder( 128 );

      appendFilteredText( filenameBuilder, testName, 25 );
      filenameBuilder.append( '-' );

      if ( !elementName.isEmpty() ) {
         appendFilteredText( filenameBuilder, elementName, 25 );
         filenameBuilder.append( '-' );
      }

      appendFilteredText( filenameBuilder, name, 50 );
      filenameBuilder.append( '-' );

      filenameBuilder.append( Instant.now().toEpochMilli() ).append( ".png" );

      return filenameBuilder.toString();
   }

   private void save( Path filename, byte[] data ) {
      try {
         Files.createDirectories( resultsDir );
         Files.write( filename, data, StandardOpenOption.CREATE_NEW );
         log.info( "Screenshot saved to '%s'", filename );
      } catch ( IOException ex ) {
         log.error( "Saving screenshot '" + filename + "'", ex );
      }
   }

   /**
    * Save a browser screenshot as PNG to the test results directory.
    * 
    * @param name Some text to identify the screenshot. This will only be part of the full file
    *           name.
    * @return The full path and file name of the saved screen shot.
    */
   public Path saveScreenshot( String name ) {
      final Path filename = resultsDir.resolve( createFilename( name, "" ) );
      this.save( filename, driver.getScreenshotAs( OutputType.BYTES ) );
      return filename;
   }

   /**
    * @param element The {@link org.openqa.selenium.WebElement} to capture the image of.
    * @param name Text to identify the file.
    * @return The full path and file name of the saved screen shot.
    */
   public Path saveScreenshot( WebElement element, String name ) {
      final Path filename =
            resultsDir.resolve( createFilename( name,
                                                Strings.nullToEmpty( element.getAttribute( "class" ) ) ) );

      this.save( filename, element.getScreenshotAs( OutputType.BYTES ) );
      return filename;
   }

}
