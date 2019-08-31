package net.ghue.jelenium.impl;

import java.nio.file.Path;
import javax.inject.Inject;
import org.openqa.selenium.remote.RemoteWebDriver;
import net.ghue.jelenium.api.ScreenshotSaver;
import net.ghue.jelenium.api.TestContext;
import net.ghue.jelenium.api.TestName;
import net.ghue.jelenium.api.WebNavigate;
import net.ghue.jelenium.api.action.ActionFactory;
import net.ghue.jelenium.api.annotation.TestResultDir;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.log.TestLog;
import net.ghue.jelenium.impl.action.ActionFactoryImpl;
import okhttp3.HttpUrl;

/**
 * This class should not contain any state directly in itself.
 * 
 * @author Luke Last
 */
final class TestContextImpl implements TestContext {

   private final JeleniumConfig config;

   private final TestLog log;

   private final TestName name;

   private final ScreenshotSaver screenshotSaver;

   private final Path testResultsDir;

   private final RemoteWebDriver webDriver;

   private final WebNavigate webNavigate;

   @Inject
   TestContextImpl( RemoteWebDriver webDriver, TestLog log, JeleniumConfig config,
                    WebNavigate webNavigate, TestName name, @TestResultDir Path testResultsDir,
                    ScreenshotSaver screenshotSaver ) {
      this.webDriver = webDriver;
      this.log = log;
      this.config = config;
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
   public JeleniumConfig getConfig() {
      return this.config;
   }

   @Override
   public TestName getName() {
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
   public HttpUrl getUrl() {
      return this.config.url();
   }

   @Override
   public RemoteWebDriver getWebDriver() {
      return webDriver;
   }

   @Override
   public WebNavigate getWebNavigate() {
      return this.webNavigate;
   }

   @Override
   public TestLog log() {
      return log;
   }
}
