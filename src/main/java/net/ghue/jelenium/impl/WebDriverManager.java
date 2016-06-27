package net.ghue.jelenium.impl;

import org.openqa.selenium.remote.RemoteWebDriver;

interface WebDriverManager {

   void giveBack( RemoteWebDriver driver );

   RemoteWebDriver take();

}
