package net.ghue.jelenium.api.action;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention( RUNTIME )
@Target( METHOD )
/**
 * @author Luke Last
 */
public @interface RetryableAction {

   double retryDelaySec() default 0;

   double retryTimeoutSec() default 0;
}
