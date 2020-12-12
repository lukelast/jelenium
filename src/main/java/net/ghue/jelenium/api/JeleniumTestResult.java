package net.ghue.jelenium.api;

public interface JeleniumTestResult {

   TestName getName();

   TestResultState getResult();

   String getWebDriverName();

   void setRetried( boolean wasRetried );

   boolean shouldTryAgain( int attempt );

}
