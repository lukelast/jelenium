package net.ghue.jelenium.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is required by Java. For actual use just use multiple
 * {@link net.ghue.jelenium.api.annotation.GuiceModule} annotations.
 *
 * @author Luke Last
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Target( ElementType.TYPE )
public @interface GuiceModules {

   GuiceModule[] value();
}
