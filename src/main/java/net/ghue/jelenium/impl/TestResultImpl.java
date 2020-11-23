package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultState;

class TestResultImpl implements JeleniumTestResult {

   private final TestName name;

   private final TestResultState result;

   private final int testRetries;

   private volatile boolean wasRetried = false;

   private final String webDriverName;

   TestResultImpl( TestName name, TestResultState result, int testRetries, String webDriverName ) {
      this.name = name;
      this.result = result;
      this.testRetries = testRetries;
      this.wasRetried = wasRetried;
      this.webDriverName = webDriverName;
   }

   @Override
   public TestName getName() {
      return this.name;
   }

   @Override
   public TestResultState getResult() {
      if ( this.wasRetried ) {
         return TestResultState.FAILED_RETRIED;
      } else {
         return this.result;
      }
   }

   @Override
   public void setRetried( boolean retried ) {
      this.wasRetried = retried;
   }

   @Override
   public boolean shouldTryAgain( int attemptsFinished ) {
      if ( this.result == TestResultState.FAILED ) {
         final int maxAttempts = this.testRetries + 1;
         return attemptsFinished < maxAttempts;
      }
      return false;
   }

   @Override
   public String getWebDriverName() {
      return this.webDriverName;
   }
}
