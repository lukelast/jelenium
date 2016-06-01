package net.ghue.jelenium.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.google.inject.BindingAnnotation;

/**
 * Used to inject the name of the test.
 * 
 * @author Luke Last
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Target( { ElementType.PARAMETER, ElementType.FIELD } )
@BindingAnnotation
public @interface TestName {}
