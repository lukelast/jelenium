package net.ghue.jelenium.api;

public interface TestArgs {

   String KEY_BROWSER = "browser";

   String KEY_HUB = "hub";

   String KEY_URL = "url";

   String getArg( String key );

   HttpUrl getSecondaryUrl( int index );

   HttpUrl getUrl();

}
