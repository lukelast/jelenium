package net.ghue.jelenium.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.TestName;

public class TestManagerImpl implements TestManager {

   private final JeleniumSettings settings;

   private final Class<? extends JeleniumTest> testClass;

   private final TestNameImpl name;

   private final Collection<JeleniumTestResult> results = new ConcurrentLinkedQueue<>();

   TestManagerImpl( Class<? extends JeleniumTest> testClass, JeleniumSettings settings ) {
      this.testClass = testClass;
      this.settings = settings;
      this.name = new TestNameImpl( testClass );
   }

   boolean isSkipped() {
      if ( !settings.getFilter().isEmpty() &&
           !name.getFullName().toLowerCase().contains( settings.getFilter() ) ) {
         return true;
      }
      return false;
   }

   @Override
   public TestName getName() {
      return this.name;
   }

   @Override
   public Stream<JeleniumTestResult> getResults() {
      return this.results.stream();
   }

   @Override
   public JeleniumTestResult run( RemoteWebDriver remoteWebDriver ) {
      final TestExecution testRun = new TestExecution( testClass, settings );
      testRun.run( remoteWebDriver );
      final TestResultImpl result = new TestResultImpl( testRun );
      this.results.add( result );
      return result;
   }

}
