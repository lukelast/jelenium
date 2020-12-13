package net.ghue.jelenium.api.suite;

import java.io.Closeable;
import javax.annotation.Nonnull;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface WebDriverSession extends Closeable {

   @Override
   void close();

   @Nonnull
   String getName();

   @Nonnull
   RemoteWebDriver getWebDriver();
}
