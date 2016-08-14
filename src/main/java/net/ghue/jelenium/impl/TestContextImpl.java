package net.ghue.jelenium.impl;

import java.nio.file.Path;
import javax.inject.Inject;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.HttpUrl;
import net.ghue.jelenium.api.JeleniumSettings;
import net.ghue.jelenium.api.ScreenshotSaver;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestLog;
import net.ghue.jelenium.api.WebNavigate;
import net.ghue.jelenium.api.action.ActionFactory;
import net.ghue.jelenium.api.annotation.TestName;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.impl.action.ActionFactoryImpl;

/**
 * This class should not contain any state directly in itself.
 * 
 * @author Luke Last
 */
final class TestContextImpl implements TestContext {

   private final TestLog log;

   private final String name;

   private final ScreenshotSaver screenshotSaver;

   private final JeleniumSettings settings;

   private final Path testResultsDir;

   private final RemoteWebDriver webDriver;

   private final WebNavigate webNavigate;

   @Inject
   TestContextImpl( RemoteWebDriver webDriver, TestLog log, JeleniumSettings testArgs,
                    WebNavigate webNavigate, @TestName String name,
                    @TestResultDir Path testResultsDir, ScreenshotSaver screenshotSaver ) {
      this.webDriver = webDriver;
      this.log = log;
      this.settings = testArgs;
      this.webNavigate = webNavigate;
      this.name = name;
      this.testResultsDir = testResultsDir;
      this.screenshotSaver = screenshotSaver;
   }

   @Override
   public ActionFactory action() {
      return new ActionFactoryImpl( this );
   }

   @Override
   public TestLog getLog() {
      return log;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public Path getResultDir() {
      return this.testResultsDir;
   }

   @Override
   public ScreenshotSaver getScreenshotSaver() {
      return this.screenshotSaver;
   }

   @Override
   public JeleniumSettings getSettings() {
      return this.settings;
   }

   @Override
   public HttpUrl getUrl() {
      return this.settings.getUrl();
   }

   @Override
   public RemoteWebDriver getWebDriver() {
      return webDriver;
   }

   @Override
   public WebNavigate getWebNavigate() {
      return this.webNavigate;
   }
}
