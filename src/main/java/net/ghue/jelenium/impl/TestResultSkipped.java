package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultState;

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
   public boolean shouldTryAgain( int attempt ) {
      return false;
   }

   @Override
   public void setRetried( boolean wasRetried ) {
      throw new IllegalStateException( "A skipped test cannot be retried." );
   }

   @Override
   public String getWebDriverName() {
      return "";
   }
}
