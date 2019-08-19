package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.TestResultState;

class TestResultImpl implements JeleniumTestResult {

   private final TestResultState result;

   private final TestName name;

   public TestResultImpl( TestExecution test ) {
      this.result = test.result;
      this.name = test.name;
   }

   @Override
   public TestResultState getResult() {
      return this.result;
   }

   @Override
   public TestName getName() {
      return this.name;
   }
}
