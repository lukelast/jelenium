package net.ghue.jelenium.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.google.inject.Module;

/**
 * Use this to annotate a {@link SeleniumTest} implementation. The test will then use your custom
 * GUICE {@link Module}.
 * 
 * @author Luke Last
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Target( ElementType.TYPE )
@Repeatable( GuiceModules.class )
public @interface GuiceModule {

   Class<? extends Module> value();
}
