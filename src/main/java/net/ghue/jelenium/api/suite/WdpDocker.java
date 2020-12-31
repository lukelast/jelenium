package net.ghue.jelenium.api.suite;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriverBuilder;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.ghue.jelenium.api.config.JeleniumConfig;
import net.ghue.jelenium.impl.Utils;
import net.ghue.jelenium.impl.suite.WebDriverSessionBase;

public final class WdpDocker implements WebDriverProvider {

   private static final class WdSessionDocker extends WebDriverSessionBase {

      private final String browser;

      private final String containerId;

      private final DockerClient docker;

      public WdSessionDocker( RemoteWebDriver driver, DockerClient docker, String containerId,
                              String browser ) {
         super( driver );
         this.containerId = containerId;
         this.docker = docker;
         this.browser = browser;
      }

      @Override
      public void close() {
         super.close();
         // With auto remove enabled stopping the container removes it.
         docker.stopContainerCmd( this.containerId ).exec();
         //docker.removeContainerCmd( id ).withForce( true ).exec();
      }

      @Override
      public String getName() {
         return "docker-" + this.browser;
      }
   }

   private static final int SELENIUM_PORT = 4444;

   /**
    * Set to 2G according to DOCS.
    * 
    * @see "https://github.com/SeleniumHQ/docker-selenium"
    */
   protected static final long SHARE_MEM_BYTES = 1024 * 1024 * 1024 * 2L;

   private DockerClient docker;

   private JeleniumConfig jeleniumConfig;

   @Override
   public void close() {
      try {
         if ( this.docker != null ) {
            this.docker.close();
         }
      } catch ( IOException ex ) {
         throw new RuntimeException( ex );
      }
   }

   protected CreateContainerCmd createContainer() {
      return docker.createContainerCmd( this.createDockerImage() )
                   .withEnv( "SCREEN_WIDTH=1440", "SCREEN_HEIGHT=1280", "START_XVFB=true" )
                   .withHostConfig( HostConfig.newHostConfig()
                                              .withPublishAllPorts( true )
                                              .withRestartPolicy( RestartPolicy.noRestart() )
                                              .withAutoRemove( true )
                                              .withShmSize( SHARE_MEM_BYTES ) );
   }

   protected DockerClient createDockerClient() {
      final DockerClientConfig clientConfig = createDockerClientConfig();

      final DockerHttpClient httpClient =
            new ApacheDockerHttpClient.Builder().dockerHost( clientConfig.getDockerHost() )
                                                .sslConfig( clientConfig.getSSLConfig() )
                                                .maxConnections( 100 )
                                                .build();

      return DockerClientImpl.getInstance( clientConfig, httpClient );
   }

   protected DockerClientConfig createDockerClientConfig() {
      // These settings can be read from docker-java.properties
      return DefaultDockerClientConfig.createDefaultConfigBuilder()
                                      //.withDockerHost( "tcp://localhost:2375" )
                                      //.withDockerTlsVerify( true )
                                      //.withDockerCertPath( "C:\\ProgramData\\DockerDesktop\\pki" )
                                      //.withDockerConfig( "/home/user/.docker" )
                                      //.withApiVersion( "1.30" ) // optional
                                      //.withRegistryUrl( "https://index.docker.io/v1/" )
                                      //.withRegistryUsername( "dockeruser" )
                                      //.withRegistryPassword( "ilovedocker" )
                                      //.withRegistryEmail( "dockeruser@github.com" )
                                      .build();
   }

   @Nonnull
   protected String createDockerImage() {
      return "selenium/standalone-" + getBrowser() + ":latest";
   }

   @Override
   public WebDriverSession createWebDriver() {
      final CreateContainerResponse result = createContainer().exec();
      System.out.println( result );

      final String containerId = result.getId();
      if ( containerId == null ) {
         throw new IllegalArgumentException();
      }

      docker.startContainerCmd( containerId ).exec();
      // SELENIUM hub needs time to start.
      // 2 seconds seems to be the minimum time required.
      Utils.sleep( 3 );

      RemoteWebDriver webDriver = (RemoteWebDriver) createWebDriverBuilder( containerId ).build();

      return new WdSessionDocker( webDriver, docker, containerId, getBrowser() );
   }

   protected RemoteWebDriverBuilder createWebDriverBuilder( String id ) {

      final Binding[] binding = docker.inspectContainerCmd( id )
                                      .exec()
                                      .getNetworkSettings()
                                      .getPorts()
                                      .getBindings()
                                      .get( ExposedPort.tcp( SELENIUM_PORT ) );
      final int port = Integer.parseInt( binding[0].getHostPortSpec() );

      final DesiredCapabilities cap = new DesiredCapabilities();
      cap.setCapability( CapabilityType.BROWSER_NAME, getBrowser() );

      return RemoteWebDriver.builder().url( "http://localhost:" + port + "/wd/hub" ).oneOf( cap );
   }

   protected String getBrowser() {
      return this.jeleniumConfig.browserName();
   }

   @Override
   public void init( JeleniumConfig jConfig ) {
      this.jeleniumConfig = jConfig;
      this.docker = this.createDockerClient();

      System.out.println( docker.versionCmd().exec().toString() );
      System.out.println( docker.infoCmd().exec().toString() );

      try {
         docker.pullImageCmd( this.createDockerImage() )
               .exec( new PullImageResultCallback() )
               .awaitCompletion();
      } catch ( InterruptedException ex ) {
         throw new RuntimeException( ex );
      }
   }

}
