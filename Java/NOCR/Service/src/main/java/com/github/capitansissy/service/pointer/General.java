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

@Path(Paths.BASE_PATH)
public class General {
  @Inject
  private RGeneral general;

  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String sayHello() {
    return general.sayHello();
  }

  @Path(Paths.INPUT_PATH)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetInput(@PathParam(PathParams.A) String name) {
    return general.GetInput(name);
  }

  @Path(Paths.GET_VERSION)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetVersion() {
    return general.GetVersion();
  }

  @Path(Paths.GET_DATE)
  @GET
  @Produces({Defaults.MediaType.TEXT_HTML})
  public String GetDate() {
    return general.GetDate();
  }

}
