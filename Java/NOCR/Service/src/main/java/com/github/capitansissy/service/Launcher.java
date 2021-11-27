package com.github.capitansissy.service;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.service.implementation.GeneralImpl;
import com.github.capitansissy.service.interfaces.restful.RGeneral;
import com.github.capitansissy.service.pointer.General;
import com.github.capitansissy.usb.UsbDetector;
import com.github.capitansissy.usb.UsbDevice;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class Launcher implements Serializable {
  private static Logger logger = new Logger();
  private static boolean IS_ATTACHED = false;

  @NotNull
  @Contract(pure = true)
  private static String companyName() {
    return "NOCR";
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
    logger.setLog("Starting RESTful Server........", Defaults.Log4J.DEBUG);
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
      // SOAP
      Endpoint.publish(String.format("%1$s://%2$s%3$s:%4$s/%5$s/%6$s",
        Defaults.URL.PROTOCOL,
        Defaults.URL.SLD,
        Defaults.URL.TLD,
        Defaults.URL.PORTS.SOAP_GENERAL,
        Defaults.URL.DIRECTORY,
        Defaults.URL.LOCATIONS.HELLOWORLD), new GeneralImpl());
      logger.setLog(String.format("General interface running on port %1$s", Defaults.URL.PORTS.SOAP_GENERAL), Defaults.Log4J.DEBUG);

      // RESTful
      HttpServer httpServer = startServer();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          logger.setLog("Shutting down the application...", Defaults.Log4J.DEBUG);
          httpServer.shutdownNow();
          logger.setLog("Done, exit.", Defaults.Log4J.DEBUG);
        } catch (Exception e) {
          logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
        }
      }));

      // block and wait shut down signal, CTRL + C
      Thread.currentThread().join();
    } catch (Exception e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.FATAL);
    }
  }

  public static void main(String[] args) throws IOException {
    UsbDetector usbDetector = new UsbDetector();
    List<UsbDevice> removableDevices = usbDetector.getRemovableDevices();
    if (removableDevices.size() > 0) {
      for (UsbDevice usbDevice : removableDevices) {
        if (usbDevice.getUuid().toLowerCase().equals(Defaults.DEVICE_ID)) {
          IS_ATTACHED = true;
        }
      }
    }

    if (!IS_ATTACHED) {
      run();
    } else {
      logger.setLog("Hardware key not detected", Defaults.Log4J.DEBUG);
      System.out.println("Hardware key not detected");
    }

  }
}
