package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultState;

class TestResultImpl implements JeleniumTestResult {

   private final TestName name;

   private final TestResultState result;

   private final int testRetries;

   private volatile boolean wasRetried = false;

   public TestResultImpl( TestExecution test ) {
      this.result = test.result;
      this.name = test.name;
      this.testRetries = test.getTestRetries();
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

   public void setRetried( boolean retried ) {
      this.wasRetried = retried;
   }

   public boolean shouldTryAgain( int attemptsFinished ) {
      if ( this.result == TestResultState.FAILED ) {
         final int maxAttempts = this.testRetries + 1;
         return attemptsFinished < maxAttempts;
      }
      return false;
   }
}
