package net.ghue.jelenium.api;

/**
 * <p>
 * TestArgs interface.
 * </p>
 *
 * @author Luke Last
 */
public interface TestArgs {

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
    * <p>
    * getArg.
    * </p>
    *
    * @param key a {@link java.lang.String} object.
    * @return a {@link java.lang.String} object.
    */
   String getArg( String key );

   /**
    * <p>
    * getSecondaryUrl.
    * </p>
    *
    * @param index a int.
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    */
   HttpUrl getSecondaryUrl( int index );

   /**
    * <p>
    * getUrl.
    * </p>
    *
    * @return a {@link net.ghue.jelenium.api.HttpUrl} object.
    */
   HttpUrl getUrl();

}
