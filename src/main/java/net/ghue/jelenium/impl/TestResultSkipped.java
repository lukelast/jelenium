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
   public TestResultState getResult() {
      return TestResultState.SKIPPED;
   }

   @Override
   public TestName getName() {
      return this.name;
   }
}
