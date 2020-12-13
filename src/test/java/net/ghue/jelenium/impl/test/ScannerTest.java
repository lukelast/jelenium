package net.ghue.jelenium.impl.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import net.ghue.jelenium.demo.SampleConfigUpdater;
import net.ghue.jelenium.demo.SampleSuite;
import net.ghue.jelenium.demo.SampleTest;

public class ScannerTest {

   @Test
   public void testFindConfigUpdaters() throws Exception {
      Assertions.assertThat( new Scanner().findConfigUpdaters() )
                .contains( SampleConfigUpdater.class );
   }

   @Test
   public void testFindSuites() throws Exception {
      Assertions.assertThat( new Scanner().findSuites() ).contains( SampleSuite.class );
   }

   @Test
   public void testFindTests() throws Exception {
      Assertions.assertThat( new Scanner().findTests() ).contains( SampleTest.class );
   }

}
