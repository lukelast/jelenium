package net.ghue.jelenium.api.test;

import java.nio.file.Path;
import org.openqa.selenium.WebElement;

public interface ScreenshotSaver {

   /**
    * Save a browser screenshot as PNG to the test results directory.
    * 
    * @param name Some text to identify the screenshot. This will only be part of the full file
    *           name.
    * @return The full path and file name of the saved screen shot.
    */
   Path saveScreenshot( String name );

   /**
    * @param element The {@link org.openqa.selenium.WebElement} to capture the image of.
    * @param name Text to identify the file.
    * @return The full path and file name of the saved screen shot.
    */
   Path saveScreenshot( WebElement element, String name );

}
