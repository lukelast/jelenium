package net.ghue.jelenium.api.log;

/**
 * Each test will have it's own {@link TestLog}.
 *
 * @author Luke Last
 */
public interface TestLog {

   /**
    * Debug is the most verbose level.
    * 
    * @return Debug level logger.
    */
   LogApi debug();

   /**
    * An error means the test has failed.
    * 
    * @return Error level logger.
    */
   LogApi error();

   /**
    * Major information events.
    * 
    * @return Info level logger.
    */
   LogApi info();

   /**
    * A warning is bad but the test can keep going.
    * 
    * @return Warning level logger.
    */
   LogApi warn();
}
