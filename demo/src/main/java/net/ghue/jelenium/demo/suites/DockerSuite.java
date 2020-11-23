package net.ghue.jelenium.demo.suites;

import java.util.Collection;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import net.ghue.jelenium.api.JeleniumTestResult;
import net.ghue.jelenium.api.TestManager;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.api.suite.JeleniumSuiteRunner;
import net.ghue.jelenium.api.suite.WebDriverProvider;

public final class DockerSuite implements JeleniumSuiteRunner {

   @Override
   public void runTests( Collection<TestManager> tests, JeleniumConfig jConfig ) throws Exception {
      DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                                           .withDockerHost( "tcp://localhost:2375" )
                                                           .withDockerTlsVerify( false )
                                                           //.withDockerCertPath( "C:\\ProgramData\\DockerDesktop\\pki" )
                                                           //.withDockerConfig( "/home/user/.docker" )
                                                           //.withApiVersion( "1.30" ) // optional
                                                           //.withRegistryUrl( "https://index.docker.io/v1/" )
                                                           //.withRegistryUsername( "dockeruser" )
                                                           //.withRegistryPassword( "ilovedocker" )
                                                           //.withRegistryEmail( "dockeruser@github.com" )
                                                           .build();
      DockerClient docker = DockerClientBuilder.getInstance( config ).build();

      System.out.println( docker.versionCmd().exec().toString() );
      System.out.println( docker.infoCmd().exec().toString() );

      final String browser = BrowserType.CHROME;

      final String image = "selenium/standalone-" + browser + "-debug:3.141";

      docker.pullImageCmd( image ).exec( new PullImageResultCallback() ).awaitCompletion();

      final WebDriverProvider driverProvider = new WebDriverProvider() {

         RemoteWebDriver webDriver;

         String containerId = "";

         @Override
         public void finished( JeleniumTestResult result, RemoteWebDriver driver ) {
            driver.quit();
            final String id = containerId;
            if ( id == null ) {
               throw new IllegalStateException();
            }
            docker.stopContainerCmd( id ).exec();
            //docker.removeContainerCmd( id ).withForce( true ).exec();
         }

         @Override
         public RemoteWebDriver get() {
            final CreateContainerResponse result =
                  docker.createContainerCmd( image )
                        .withEnv( "SCREEN_WIDTH=1440", "SCREEN_HEIGHT=1280", "START_XVFB=true" )
                        .withHostConfig( HostConfig.newHostConfig()
                                                   .withPublishAllPorts( true )
                                                   .withRestartPolicy( RestartPolicy.noRestart() )
                                                   .withAutoRemove( true )
                                                   .withShmSize( 1024 * 1024 * 1024 * 2L ) )
                        .exec();

            System.out.println( result );
            final String id = result.getId();
            if ( id == null ) {
               throw new IllegalArgumentException();
            }
            containerId = id;

            docker.startContainerCmd( id ).exec();
            // Selenium hub needs time to start.
            // 2 seconds seems to be the minimum time required.
            try {
               Thread.sleep( 3_000 );
            } catch ( InterruptedException ex ) {
               throw new RuntimeException( ex );
            }

            final Binding[] binding = docker.inspectContainerCmd( id )
                                            .exec()
                                            .getNetworkSettings()
                                            .getPorts()
                                            .getBindings()
                                            .get( ExposedPort.tcp( 4444 ) );
            final int port = Integer.parseInt( binding[0].getHostPortSpec() );

            final DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability( CapabilityType.BROWSER_NAME, browser );
            // Test to pass name info to the results.
            cap.setCapability( "name", "docker" );

            webDriver =
                  (RemoteWebDriver) RemoteWebDriver.builder()
                                                   .url( "http://localhost:" + port + "/wd/hub" )
                                                   .oneOf( cap )
                                                   .build();
            return webDriver;
         }

      };

      for ( TestManager test : tests ) {
         test.runWithRetries( driverProvider );
      }

      driverProvider.get().quit();

   }

}
