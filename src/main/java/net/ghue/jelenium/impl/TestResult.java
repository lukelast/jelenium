package net.ghue.jelenium.impl;

enum TestResult {
   /**
    * There was an error initializing the test environment. The test could not even run.
    */
   ERROR,
   /**
    * The test failed.
    */
   FAILED,
   /**
    * Everything is good.
    */
   PASSED,
   /**
    * The test has not run yet.
    */
   NOT_RUN,
   /**
    * The test was skipped.
    */
   SKIPPED;
}
