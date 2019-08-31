package net.ghue.jelenium.impl;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import javax.annotation.Nullable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.demo.SampleTest;
import net.ghue.jelenium.log.TestLogMock;

class ScreenshotSaverImplTest {

   private static Path resultsDir;

   private static final String SCREEN_SHOT_DATA = "hello";

   private static final TestName TEST_NAME = new TestNameImpl( SampleTest.class );

   @AfterAll
   static void cleanup() throws IOException {
      Files.delete( resultsDir );
   }

   @BeforeAll
   static void init() throws IOException {
      resultsDir = Files.createTempDirectory( ScreenshotSaverImplTest.class.getSimpleName() );
   }

   @Test
   void testSaveScreenshotString() throws Exception {

      final TakesScreenshot webdriver = new TakesScreenshot() {

         @SuppressWarnings( "unchecked" )
         @Override
         public <X> X getScreenshotAs( @Nullable OutputType<X> target ) throws WebDriverException {
            return (X) SCREEN_SHOT_DATA.getBytes();
         }
      };

      ScreenshotSaverImpl screenshotSaver =
            new ScreenshotSaverImpl( webdriver,
                                     new TestLogMock(),
                                     resultsDir,
                                     TEST_NAME,
                                     Clock.fixed( Instant.EPOCH, ZoneOffset.UTC ) );

      screenshotSaver.saveScreenshot( "NAME" );

      verify( "SampleTest-NAME-700101@00-00-00.png" );
   }

   @Test
   void testSaveScreenshotWebElementString() throws Exception {
      WebElement element = mock( WebElement.class );
      when( element.getScreenshotAs( OutputType.BYTES ) ).thenReturn( SCREEN_SHOT_DATA.getBytes() );
      when( element.getAttribute( "class" ) ).thenReturn( "ElementClassName" );

      ScreenshotSaverImpl screenshotSaver =
            new ScreenshotSaverImpl( mock( TakesScreenshot.class ),
                                     new TestLogMock(),
                                     resultsDir,
                                     TEST_NAME,
                                     Clock.fixed( Instant.EPOCH, ZoneOffset.UTC ) );

      screenshotSaver.saveScreenshot( element, "NAME" );

      verify( "SampleTest-ElementClassName-NAME-700101@00-00-00.png" );
   }

   private void verify( String filename ) throws IOException {
      final Path expectedFilePath = resultsDir.resolve( filename );

      Assertions.assertThat( expectedFilePath )
                .exists()
                .isRegularFile()
                .hasContent( SCREEN_SHOT_DATA );

      Files.delete( expectedFilePath );
   }

}
