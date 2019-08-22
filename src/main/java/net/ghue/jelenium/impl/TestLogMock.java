package net.ghue.jelenium.impl;

import net.ghue.jelenium.api.TestLog;

final class TestLogMock implements TestLog {

   @Override
   public void debug( String message, Object... args ) {}

   @Override
   public void error( String message, Throwable ex ) {}

   @Override
   public void info( String message, Object... args ) {}

   @Override
   public void warn( String message, Object... args ) {}

}
