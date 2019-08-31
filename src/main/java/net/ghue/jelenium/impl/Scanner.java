package net.ghue.jelenium.impl;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import net.ghue.jelenium.api.JeleniumTest;
import net.ghue.jelenium.api.config.JeleniumConfigUpdater;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;

/**
 * Searches the class path for available tests to be run.
 *
 * @author Luke Last
 */
public final class Scanner {

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

   public static List<Class<? extends JeleniumConfigUpdater>> findSettings() {
      try {
         return scanAndLoadClasses().filter( JeleniumConfigUpdater.class::isAssignableFrom )
                                    .map( cl -> cl.<JeleniumConfigUpdater> asSubclass( JeleniumConfigUpdater.class ) )
                                    .collect( ImmutableList.toImmutableList() );
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   static List<Class<? extends JeleniumSuiteRunner>> findSuites() throws IOException {
      return scanAndLoadClasses().filter( JeleniumSuiteRunner.class::isAssignableFrom )
                                 .map( cl -> cl.<JeleniumSuiteRunner> asSubclass( JeleniumSuiteRunner.class ) )
                                 .collect( ImmutableList.toImmutableList() );
   }

   /**
    * Scan all available classes on the class-path looking for any that implement
    * {@link JeleniumTest}.
    *
    * @return List of classes.
    * @throws java.io.IOException if any.
    */
   static List<Class<? extends JeleniumTest>> findTests() {
      try {
         return scanAndLoadClasses().filter( JeleniumTest.class::isAssignableFrom )
                                    .map( cl -> cl.<JeleniumTest> asSubclass( JeleniumTest.class ) )
                                    .collect( ImmutableList.toImmutableList() );
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   private static Stream<Class<?>> load( ClassInfo ci ) {
      try {
         return Stream.of( ci.load() );
      } catch ( Throwable ex ) {
         return Stream.empty();
      }
   }

   private static Stream<Class<?>> scanAndLoadClasses() throws IOException {
      return ClassPath.from( Thread.currentThread().getContextClassLoader() )
                      .getTopLevelClasses()
                      .stream()
                      .filter( Scanner::shouldCheck )
                      //.peek( System.out::println )
                      .flatMap( Scanner::load )
                      // No abstract classes.
                      .filter( cl -> !Modifier.isAbstract( cl.getModifiers() ) )
                      // No interfaces.
                      .filter( cl -> !cl.isInterface() );
   }

   /**
    * Filter out library classes so we don't bother loading them.
    */
   private static boolean shouldCheck( ClassInfo ci ) {
      return CLASSES_TO_IGNORE.stream().noneMatch( ci.getName()::startsWith );
   }

}
