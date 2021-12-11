package com.github.capitansissy.service;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.constants.webservice.restful.Paths;
import com.github.capitansissy.constants.webservice.soap.Interfaces;
import com.github.capitansissy.database.layer.Business;
import com.github.capitansissy.enumeration.Database;
import com.github.capitansissy.service.implementation.GeneralImpl;
import com.github.capitansissy.service.interfaces.restful.RGeneral;
import com.github.capitansissy.service.pointer.General;
import com.github.capitansissy.usb.UsbDetector;
import com.github.capitansissy.usb.UsbDevice;
import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

public class Launcher implements Serializable {
  private static Logger logger = new Logger();
  private static boolean IS_ATTACHED = false;

  @NotNull
  @Contract(pure = true)
  private static String companyName() {
    return "NOCR";
  }

  // Grizzly HTTPS server
  private static HttpServer startSecureServer() throws IOException {
    SSLContextConfigurator configurator = new SSLContextConfigurator();
    InputStream keyStoreAsStream;
    InputStream trustStoreAsStream;

    final ResourceConfig config = new ResourceConfig();
    config.register(General.class);
    config.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(GeneralImpl.class).to(RGeneral.class);
      }
    });

    if (Tools.isDebugging()) {
      keyStoreAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Defaults.SSL.KEYSTORE_FILE_PATH);
      trustStoreAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Defaults.SSL.TRUSTSTORE_FILE_PATH);
    } else {
      keyStoreAsStream = ClassLoader.getSystemResourceAsStream(Defaults.SSL.KEYSTORE_FILE_PATH);
      trustStoreAsStream = ClassLoader.getSystemResourceAsStream(Defaults.SSL.TRUSTSTORE_FILE_PATH);
    }
    if (keyStoreAsStream == null)
      throw new Error("Could not get Keystore!");

    configurator.setKeyStoreBytes(ByteStreams.toByteArray(Objects.requireNonNull(keyStoreAsStream)));
    configurator.setKeyStorePass(Objects.requireNonNull(Defaults.SSL.KEYSTORE_PASSWORD));
    configurator.setTrustStoreBytes(ByteStreams.toByteArray(Objects.requireNonNull(trustStoreAsStream)));
    configurator.setTrustStorePass(Objects.requireNonNull(Defaults.SSL.TRUSTSTORE_PASSWORD));
    configurator.setSecurityProtocol("TLS");
    logger.setLog("Starting Jersey-Grizzly2 JAX-RS (RESTful) Secure server...", Defaults.Log4J.DEBUG);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(String.format("%1$s://%2$s%3$s:%4$s/%5$s/%6$s",
      Defaults.URL.PROTOCOL,
      Defaults.URL.SLD,
      Defaults.URL.TLD,
      Defaults.URL.PORTS.RESTFUL_GENERAL,
      Defaults.URL.DIRECTORY,
      Defaults.URL.LOCATIONS.HELLOWORLD)), config, true,
      new SSLEngineConfigurator(configurator).setWantClientAuth(false)
        .setClientMode(false)
        .setNeedClientAuth(false));
  }

  // Grizzly HTTP server
  static HttpServer startServer() {
    final ResourceConfig config = new ResourceConfig();
    config.register(General.class);
    config.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(GeneralImpl.class).to(RGeneral.class);
      }
    });
//    config.register(AutoScan.class);
    logger.setLog("Starting Jersey-Grizzly2 JAX-RS (RESTful) Server...", Defaults.Log4J.DEBUG);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(String.format("%1$s://%2$s%3$s:%4$s/%5$s/%6$s",
      Defaults.URL.PROTOCOL,
      Defaults.URL.SLD,
      Defaults.URL.TLD,
      Defaults.URL.PORTS.RESTFUL_GENERAL,
      Defaults.URL.DIRECTORY,
      Defaults.URL.LOCATIONS.HELLOWORLD)), config);
  }

  private static void run() {
    try {
      logger.setLog(String.format("Hello %s", companyName()), Defaults.Log4J.DEBUG);
      System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

      // At startup we check the database and then create connection pool.
      try {
        new Business().InitializeDatabaseAsPrimitive();
        new Business().InitializeDatabaseAsSecondary(Database.Data.getCode());
        new Business().InitializeDatabaseAsSecondary(Database.Log.getCode());
      } catch (Exception e) {
        logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
        System.exit(1);
      }

      /*=-=-=-=-=-[ Initialize SSL ]-=-=-=-=-=*/
      SSLContext sslContext = SSLContext.getInstance(Defaults.URL.PROTOCOL);
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream keyStoreAsStream;
      if (Tools.isDebugging()) {
        keyStoreAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Defaults.SSL.KEYSTORE_FILE_PATH);
      } else {
        keyStoreAsStream = ClassLoader.getSystemResourceAsStream(Defaults.SSL.KEYSTORE_FILE_PATH);
      }
      keyStore.load(keyStoreAsStream, Objects.requireNonNull(Defaults.SSL.KEYSTORE_PASSWORD).toCharArray());
      keyManagerFactory.init(keyStore, Objects.requireNonNull(Defaults.SSL.KEYSTORE_PASSWORD).toCharArray());
      KeyManager[] keyManagers;
      keyManagers = keyManagerFactory.getKeyManagers();
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);
      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
      sslContext.init(keyManagers, trustManagers, new SecureRandom());

      /*=-=-=-=-=-[ SOAP - Without SSL ]-=-=-=-=-=*/
//      Endpoint.publish(String.format("%1$s://%2$s%3$s:%4$s/%5$s/%6$s",
//        Defaults.URL.PROTOCOL,
//        Defaults.URL.SLD,
//        Defaults.URL.TLD,
//        Defaults.URL.PORTS.SOAP_GENERAL,
//        Defaults.URL.DIRECTORY,
//        Defaults.URL.LOCATIONS.HELLOWORLD), new GeneralImpl());

      /*=-=-=-=-=-[ SOAP - With Self-Signed SSL ]-=-=-=-=-=*/
      Endpoint endpoint = Endpoint.create(new GeneralImpl());
      HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext);
      HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(Defaults.URL.SLD,
          Integer.parseInt(Defaults.URL.PORTS.SOAP_GENERAL)),
        Integer.parseInt(Defaults.URL.PORTS.SOAP_GENERAL));
      httpsServer.setHttpsConfigurator(httpsConfigurator);
      HttpContext httpContext = httpsServer.createContext(String.format("/%1$s/%2$s%3$s",
        Defaults.URL.DIRECTORY,
        Defaults.URL.LOCATIONS.HELLOWORLD,
        Interfaces.GENERAL_WEBSERVICE_NAME));
      httpsServer.start();
      endpoint.publish(httpContext);
      logger.setLog(String.format("General interface running on port %1$s", Defaults.URL.PORTS.SOAP_GENERAL), Defaults.Log4J.DEBUG);

      /*=-=-=-=-=-[ RESTful ]-=-=-=-=-=*/
      HttpServer httpServer = startSecureServer();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          logger.setLog("Shutting down the application...", Defaults.Log4J.DEBUG);
          httpServer.shutdownNow();
          logger.setLog("Done, exit.", Defaults.Log4J.DEBUG);
        } catch (Exception e) {
          logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
        }
      }));
      // Draw ASCII */
      System.out.println();
      System.out.println();
      System.out.println("888    888          888 888               888       888                  888      888 ");
      System.out.println("888    888          888 888               888   o   888                  888      888 ");
      System.out.println("888    888          888 888               888  d8b  888                  888      888 ");
      System.out.println("8888888888  .d88b.  888 888  .d88b.       888 d888b 888  .d88b.  888d888 888  .d88888 ");
      System.out.println("888    888 d8P  Y8b 888 888 d88\"\"88b      888d88888b888 d88\"\"88b 888P\"   888 d88\" 888 ");
      System.out.println("888    888 88888888 888 888 888  888      88888P Y88888 888  888 888     888 888  888 ");
      System.out.println("888    888 Y8b.     888 888 Y88..88P      8888P   Y8888 Y88..88P 888     888 Y88b 888 ");
      System.out.println("888    888  \"Y8888  888 888  \"Y88P\"       888P     Y888  \"Y88P\"  888     888  \"Y88888 ");
      System.out.println("|");
      System.out.println("|");
      System.out.println(String.format("|-- SOAP:    https://%s", String.format("%1$s%2$s:%3$s/%4$s/%5$s%6$s?wsdl", Defaults.URL.SLD, Defaults.URL.TLD, Defaults.URL.PORTS.SOAP_GENERAL, Defaults.URL.DIRECTORY, Defaults.URL.LOCATIONS.HELLOWORLD, Interfaces.GENERAL_WEBSERVICE_NAME)));
      System.out.println("|");
      System.out.println(String.format("|-- RESTful: https://%s", String.format("%1$s%2$s:%3$s/%4$s/%5$s%6$s", Defaults.URL.SLD, Defaults.URL.TLD, Defaults.URL.PORTS.RESTFUL_GENERAL, Defaults.URL.DIRECTORY, Defaults.URL.LOCATIONS.HELLOWORLD, Paths.BASE_PATH.substring(1))));
      System.out.println();
      System.out.print("Press <CTRL + C> to exit");
      // block and wait shut down signal, CTRL + C
      Thread.currentThread().join();
    } catch (Exception e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.FATAL);
    }
  }

  public static void main(String[] args) {
    try {
      UsbDetector usbDetector = new UsbDetector();
      List<UsbDevice> removableDevices = usbDetector.getRemovableDevices();
      if (removableDevices.size() > 0) {
        for (UsbDevice usbDevice : removableDevices) {
          if (usbDevice.getUuid().toLowerCase().equals(Defaults.DEVICE_ID)) {
            IS_ATTACHED = true;
          }
        }
      }

      if (IS_ATTACHED) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        if (arguments.contains("-Dfile.encoding=UTF-8")) {
          run();
        } else {
          logger.setLog("This is a multilingual program. Use \"-Dfile.encoding=UTF-8\" to display messages correctly.", Defaults.Log4J.DEBUG);
          System.out.println("This is a multilingual program. Use \"-Dfile.encoding=UTF-8\" to display messages correctly.");
        }
      } else {
        logger.setLog("Hardware key not detected", Defaults.Log4J.DEBUG);
        System.out.println("Hardware key not detected");
      }

    } catch (Exception e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
    }
  }
}
