package net.ghue.jelenium.api.test;

public interface JeleniumTestResult {

   TestName getName();

   TestResultState getResult();

   String getWebDriverName();

   void setRetried( boolean wasRetried );

   boolean shouldTryAgain( int attempt );

}
