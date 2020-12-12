package net.ghue.jelenium.impl.test;

import net.ghue.jelenium.api.test.JeleniumTestResult;
import net.ghue.jelenium.api.test.TestName;
import net.ghue.jelenium.api.test.TestResultState;

public class TestResultSkipped implements JeleniumTestResult {

   private final TestName name;

   public TestResultSkipped( TestName name ) {
      this.name = name;
   }

   @Override
   public TestName getName() {
      return this.name;
   }

   @Override
   public TestResultState getResult() {
      return TestResultState.SKIPPED;
   }

   @Override
   public String getWebDriverName() {
      return "";
   }

   @Override
   public void setRetried( boolean wasRetried ) {
      throw new IllegalStateException( "A skipped test cannot be retried." );
   }

   @Override
   public boolean shouldTryAgain( int attempt ) {
      return false;
   }
}
