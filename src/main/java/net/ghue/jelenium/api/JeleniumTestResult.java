package net.ghue.jelenium.api;

public interface JeleniumTestResult {

   TestName getName();

   TestResultState getResult();

   boolean shouldTryAgain( int attempt );

   void setRetried( boolean wasRetried );

   String getWebDriverName();

}
