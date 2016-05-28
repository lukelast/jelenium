package net.ghue.jelenium.api;

import org.openqa.selenium.WebDriver;

public interface TestContext {

   TestLog getLog();

   TestArgs getTestArgs();

   HttpUrl getUrl();

   WebDriver getWebDriver();

   WebNavigate getWebNavigate();
}
