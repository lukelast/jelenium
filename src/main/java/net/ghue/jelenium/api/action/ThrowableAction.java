package net.ghue.jelenium.api.action;

import java.util.function.Supplier;
import com.google.common.base.Throwables;

@FunctionalInterface
public interface ThrowableAction<R> {

   R execute() throws Throwable;

   default Supplier<R> toSupplier() {
      return () -> {
         try {
            return execute();
         } catch ( Throwable ex ) {
            Throwables.throwIfUnchecked( ex );
            throw new RuntimeException( ex );
         }
      };
   }
}
