package com.github.capitansissy.service;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.webservice.restful.Paths;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class TestGeneral {
  private static HttpServer httpServer;
  private static WebTarget target;

  @BeforeClass
  public static void beforeAllTests() {
    httpServer = Launcher.startServer();
    Client client = ClientBuilder.newClient();
    target = client.target(URI.create(String.format("%1$s://%2$s%3$s:%4$s/%5$s/%6$s",
      Defaults.URL.PROTOCOL,
      Defaults.URL.SLD,
      Defaults.URL.TLD,
      Defaults.URL.PORTS.RESTFUL_GENERAL,
      Defaults.URL.DIRECTORY,
      Defaults.URL.LOCATIONS.HELLOWORLD)));
  }

//  @Test
//  public void testHello() {
//    String response = target.path(Paths.BASE_PATH).request().get(String.class);
//    assertEquals("Hello world!", response);
//  }
//
//  @Test
//  public void testHelloName() {
//    String response = target.path(Paths.BASE_PATH.concat("/capitansissy")).request().get(String.class);
//    assertEquals("Hello capitansissy!", response);
//  }
//
//  @Test
//  public void testHelloHK2() {
//    String response = target.path(Paths.BASE_PATH.concat(Paths.GET_VERSION)).request().get(String.class);
//    assertEquals("Hello world from HK2", response);
//  }

  @AfterClass
  public static void afterAllTests() {
    httpServer.shutdownNow();
  }


}
