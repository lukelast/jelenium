package net.ghue.jelenium.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.common.truth.Truth;
import net.ghue.jelenium.demo.SampleSuite;
import net.ghue.jelenium.demo.SampleTest;

public class ScannerTest {

   @Test
   public void testFindSuites() throws Exception {
      Truth.assertThat( Scanner.findSuites() ).contains( SampleSuite.class );
   }

   @Test
   public void testFindTests() throws Exception {
      Assertions.assertThat( Scanner.findTests() ).contains( SampleTest.class );
   }

}
