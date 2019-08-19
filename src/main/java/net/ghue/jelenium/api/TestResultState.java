package net.ghue.jelenium.api;

public enum TestResultState {
   /**
    * There was an error initializing the test environment. The test could not even run.
    */
   ERROR,

   /**
    * The test failed.
    */
   FAILED,

   /**
    * The test failed, but was then run again.
    */
   FAILED_RETRIED,

   /**
    * The test has not run yet.
    */
   NOT_RUN,

   /**
    * Everything is good.
    */
   PASSED,

   /**
    * The test was skipped.
    */
   SKIPPED;
}
