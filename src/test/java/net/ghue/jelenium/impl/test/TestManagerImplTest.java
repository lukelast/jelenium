package net.ghue.jelenium.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.demo.SampleTest;

class TestManagerImplTest {

   private static Path tempDir;

   @BeforeAll
   public static void setup() throws IOException {
      tempDir = Files.createTempDirectory( "" );
   }

   @AfterAll
   public static void tearDown() throws IOException {
      MoreFiles.deleteRecursively( tempDir, RecursiveDeleteOption.ALLOW_INSECURE );
   }

   TestManagerImpl build() throws Exception {
      JeleniumConfig config = Mockito.mock( JeleniumConfig.class );
      Mockito.when( config.results() ).thenReturn( tempDir );
      return new TestManagerImpl( SampleTest.class, config );
   }

   @Test
   void testBuildTestResultsDirExists() throws Exception {
      TestManagerImpl tmi = build();
      Files.createDirectories( tempDir.resolve( "SampleTest-abc" ) );
      Files.createDirectories( tempDir.resolve( "SampleTest-abc-1" ) );
      Files.createDirectories( tempDir.resolve( "SampleTest-abc-2" ) );

      Path resultsDir = tmi.buildTestResultsDir( "abc", 1 );

      assertEquals( "SampleTest-abc-3", resultsDir.getFileName().toString() );
   }

   @Test
   void testBuildTestResultsDirRetry() throws Exception {
      TestManagerImpl tmi = build();
      Path resultsDir = tmi.buildTestResultsDir( "", 2 );
      assertEquals( "SampleTest-try2", resultsDir.getFileName().toString() );
   }

}
