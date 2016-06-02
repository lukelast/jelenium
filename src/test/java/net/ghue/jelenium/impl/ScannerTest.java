package net.ghue.jelenium.impl;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import net.ghue.jelenium.demo.SampleTest;

public class ScannerTest {

   @SuppressWarnings( "unchecked" )
   @Test
   public void testFindTests() throws Exception {
      Assertions.assertThat( Scanner.findTests() ).contains( SampleTest.class );
   }

}
