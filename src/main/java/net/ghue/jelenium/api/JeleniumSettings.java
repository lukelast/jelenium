package net.ghue.jelenium.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * <p>
 * TestArgs interface.
 * </p>
 *
 * @author Luke Last
 */
public interface JeleniumSettings {

   Path DEFAULT_RESULTS_DIR = Paths.get( ".", "results" ).toAbsolutePath().normalize();

   /**
    * Constant <code>KEY_BROWSER="browser"</code>
    */
   String KEY_BROWSER = "browser";

   /**
    * Constant <code>KEY_HUB="hub"</code>
    */
   String KEY_HUB = "hub";

   /**
    * Constant <code>KEY_URL="url"</code>
    */
   String KEY_URL = "url";

   /**
    * The directory that stores all the test results.
    */
   String KEY_RESULTS_DIR = "results";

   /**
    * @return The root directory for all test results.
    */
   Path getResultsDir();

   /**
    * Get the value of a setting using the key.
    *
    * @param key a {@link java.lang.String} object.
    * @return The setting value. If the setting does not exist empty string will be returned.
    */
   String get( String key );

   /**
    * <p>
    * getSecondaryUrl.
    * </p>
    *
    * @param index If the key is "url2" then the index is 2.
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    */
   Optional<HttpUrl> getSecondaryUrl( int index );

   /**
    * <p>
    * getUrl.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    */
   HttpUrl getUrl();

}
