package net.ghue.jelenium.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * <p>
 * Interface for JELENIUM settings. These settings specified at launch time and immutable.
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
    * Only tests that match this filter will be executed.
    */
   String KEY_FILTER = "filter";

   /**
    * Constant <code>KEY_HUB="hub"</code>
    */
   String KEY_HUB = "hub";

   /**
    * The directory that stores all the test results.
    */
   String KEY_RESULTS_DIR = "results";

   /**
    * Constant <code>KEY_URL="url"</code>
    */
   String KEY_URL = "url";

   /**
    * Get the value of a setting using the key.
    *
    * @param key a {@link java.lang.String} object.
    * @return The setting value. If the setting does not exist empty string will be returned.
    */
   String get( String key );

   /**
    * Empty string means no filter so run all tests.
    * 
    * @return Never {@code null}.
    * @see #KEY_FILTER
    */
   String getFilter();

   /**
    * @return The root directory for all test results.
    */
   Path getResultsDir();

   /**
    * <p>
    * Fetch a secondary URL that is not the primary {@link #getUrl()}.
    * </p>
    *
    * @param number Which URL to get. If the setting key is <b>url2</b> then the value should be 2.
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    * @see #getUrl()
    */
   Optional<HttpUrl> getSecondaryUrl( int number );

   /**
    * <p>
    * Get the primary URL under test.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    * @see #getSecondaryUrl(int)
    */
   HttpUrl getUrl();

   /**
    * Check if the given value matches the actual value for the key. Uses
    * {@link String#equalsIgnoreCase(String)} for case insensitive equality test.
    * 
    * @param key
    * @param expectedValue
    * @return {@code true} if the actual value matches the expected value.
    */
   boolean is( String key, String expectedValue );

}
