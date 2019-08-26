package net.ghue.jelenium.log;

import net.ghue.jelenium.api.log.LogApi;
import net.ghue.jelenium.api.log.TestLog;

public final class TestLogMock implements TestLog {

   @Override
   public LogApi debug() {
      return new LogApiMock();
   }

   @Override
   public LogApi error() {
      return new LogApiMock();
   }

   @Override
   public LogApi info() {
      return new LogApiMock();
   }

   @Override
   public LogApi warn() {
      return new LogApiMock();
   }
}
