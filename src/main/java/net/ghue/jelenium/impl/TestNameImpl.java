package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.TestName;

/**
 * @author Luke Last
 */
final class TestNameImpl implements TestName {

   private final Class<? extends JeleniumTest> testClass;

   TestNameImpl( Class<? extends JeleniumTest> testClass ) {
      this.testClass = testClass;
   }

   @Override
   public String getFullName() {
      return testClass.getCanonicalName();
   }

   @Override
   public String getShortName() {
      return testClass.getSimpleName();
   }

   @Override
   public String toString() {
      return this.getFullName();
   }

}
