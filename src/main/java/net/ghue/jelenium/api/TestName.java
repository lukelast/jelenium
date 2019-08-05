package net.ghue.jelenium.api;

/**
 * The name of a test, this is automatically derived from the {@link JeleniumTest} class name.
 * 
 * @author Luke Last
 */
public interface TestName {

   String getFullName();

   String getShortName();

}
