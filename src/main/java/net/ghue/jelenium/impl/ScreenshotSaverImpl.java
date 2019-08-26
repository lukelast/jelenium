package net.ghue.jelenium.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import com.google.common.base.Strings;
import net.ghue.jelenium.api.ScreenshotSaver;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.api.log.TestLog;

/**
 * @author Luke Last
 */
final class ScreenshotSaverImpl implements ScreenshotSaver {

   private final Clock clock;

   private final TakesScreenshot driver;

   private final TestLog log;

   private final Path resultsDir;

   private final TestName testName;

   @Inject
   ScreenshotSaverImpl( TakesScreenshot driver, TestLog log, @TestResultDir Path resultsDir,
                        TestName testName, Clock clock ) {
      this.driver = driver;
      this.log = log;
      this.resultsDir = resultsDir;
      this.testName = testName;
      this.clock = clock;
   }

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
         } else if ( ch == '.' || ch == ':' ) {
            output.append( '-' );
            charsAdded++;
         } else if ( ch == ' ' ) {
            output.append( '_' );
            charsAdded++;
         }
         if ( limit < charsAdded ) {
            break;
         }
      }
   }

   private String createFilename( String name, String elementName ) {
      final StringBuilder filenameBuilder = new StringBuilder( 128 );

      appendFilteredText( filenameBuilder, testName.getShortName(), 25 );
      filenameBuilder.append( '-' );

      if ( !elementName.isEmpty() ) {
         appendFilteredText( filenameBuilder, elementName, 25 );
         filenameBuilder.append( '-' );
      }

      appendFilteredText( filenameBuilder, name, 50 );
      filenameBuilder.append( '-' );
      appendFilteredText( filenameBuilder,
                          LocalDateTime.now( clock )
                                       .format( DateTimeFormatter.ISO_LOCAL_DATE_TIME ),
                          40 );
      filenameBuilder.append( ".png" );

      return filenameBuilder.toString();
   }

   private void save( Path filename, byte[] data ) {
      try {
         Files.createDirectories( resultsDir );
         Files.write( filename, data, StandardOpenOption.CREATE_NEW );
         log.info().msg( "Screenshot saved to '%s'", filename ).log();
      } catch ( IOException ex ) {
         log.error().msg( "Saving screenshot '%s'", filename ).ex( ex ).log();
      }
   }

   /**
    * Save a browser screenshot as PNG to the test results directory.
    * 
    * @param name Some text to identify the screenshot. This will only be part of the full file
    *           name.
    * @return The full path and file name of the saved screen shot.
    */
   @Override
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
   @Override
   public Path saveScreenshot( WebElement element, String name ) {
      final Path filename =
            resultsDir.resolve( createFilename( name,
                                                Strings.nullToEmpty( element.getAttribute( "class" ) ) ) );

      this.save( filename, element.getScreenshotAs( OutputType.BYTES ) );
      return filename;
   }

}
