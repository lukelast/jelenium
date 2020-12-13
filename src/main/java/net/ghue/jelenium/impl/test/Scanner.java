package net.ghue.jelenium.impl.test;

import static com.google.common.base.Predicates.not;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.test.JeleniumTest;

/**
 * Searches the class path for classes that implement certain interfaces.
 *
 * @author Luke Last
 */
public class Scanner {

   /**
    * These are packages that will never contain the classes we are looking for so ignore them to
    * improve performance.
    */
   private static final List<String> CLASSES_TO_IGNORE =
         ImmutableList.of( "cern.",
                           "com.gargoylesoftware.htmlunit.",
                           "com.google.",
                           "com.steadystate.css.",
                           "com.sun.",
                           "com.thoughtworks.selenium.",
                           "java_cup.",
                           "javax.",
                           "junit.",
                           "net.ghue.jelenium.api.",
                           "net.ghue.jelenium.impl.",
                           "net.sf.cglib.",
                           "net.sourceforge.htmlunit.",
                           "netscape.javascript.",
                           "okio.",
                           "org.aopalliance.",
                           "org.apache.",
                           "org.assertj.",
                           "org.codehaus.",
                           "org.cyberneko.html.",
                           "org.eclipse.jetty.",
                           "org.hamcrest.",
                           "org.hibernate.",
                           "org.jboss.netty.",
                           "org.junit.",
                           "org.openqa.selenium.",
                           "org.w3c.",
                           "org.webbitserver.",
                           "org.xml." );

   /**
    * Load a {@link ClassInfo} into a {@link Class} instance.
    */
   private static Stream<Class<?>> load( ClassInfo ci ) {
      try {
         return Stream.of( ci.load() );
      } catch ( Throwable ex ) {
         return Stream.empty();
      }
   }

   /**
    * Return all classes on the Java CLASSPATH after doing some filtering.
    */
   private static Stream<Class<?>> scanAndLoadClasses() {
      try {
         return ClassPath.from( Thread.currentThread().getContextClassLoader() )
                         .getTopLevelClasses()
                         .stream()
                         .filter( Scanner::shouldCheck )
                         //.peek( System.out::println )
                         .flatMap( Scanner::load )
                         // No abstract classes.
                         .filter( cl -> !Modifier.isAbstract( cl.getModifiers() ) )
                         // No interfaces.
                         .filter( not( Class::isInterface ) );
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   /**
    * Filter out library classes so we don't bother loading them.
    */
   private static boolean shouldCheck( ClassInfo ci ) {
      return CLASSES_TO_IGNORE.stream().noneMatch( ci.getName()::startsWith );
   }

   private <T> Stream<Class<? extends T>> findClasses( Class<T> type ) {
      return scanAndLoadClasses().filter( type::isAssignableFrom )
                                 .map( cl -> cl.<T> asSubclass( type ) );
   }

   public Stream<Class<? extends JeleniumConfigUpdater>> findConfigUpdaters() {
      return findClasses( JeleniumConfigUpdater.class );
   }

   Stream<Class<? extends JeleniumSuiteRunner>> findSuites() {
      return findClasses( JeleniumSuiteRunner.class );
   }

   /**
    * Scan all available classes on the class-path looking for any that implement
    * {@link JeleniumTest}.
    *
    * @return List of classes.
    */
   Stream<Class<? extends JeleniumTest>> findTests() {
      return findClasses( JeleniumTest.class );
   }

}
