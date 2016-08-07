package net.ghue.jelenium.impl;

import java.io.Closeable;
import org.openqa.selenium.remote.RemoteWebDriver;

interface WebDriverManager extends Closeable {

   /**
    * @param driver This driver can not be used after being given back. It's up to the manager to
    *           decide if it will be closed or reused.
    */
   void giveBack( RemoteWebDriver driver );

   /**
    * @return Allocate a driver. It could be new or taken from a pool.
    */
   RemoteWebDriver take();

}