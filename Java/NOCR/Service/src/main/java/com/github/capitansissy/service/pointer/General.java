package com.github.capitansissy.service.pointer;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.webservice.restful.PathParams;
import com.github.capitansissy.constants.webservice.restful.Paths;
import com.github.capitansissy.service.interfaces.restful.RGeneral;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path(Paths.A)
public class General {
  @Inject
  private RGeneral general;

  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String sayHello() {
    return general.sayHello();
  }

  @Path(Paths.B)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetInput(@PathParam(PathParams.A) String name) {
    return general.GetInput(name);
  }

  @Path(Paths.C)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetVersion() {
    return general.GetVersion();
  }

  @Path(Paths.D)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetDate() {
    return general.GetDate();
  }

}
