package rest.utilities;

import entities.Option;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/options")
public class OptionResource {

    private static final OptionBusiness optB = new OptionBusiness();

    // POST /options -> 200 | 404
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOption(Option option) {
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Empty or unreadable body")
                    .build();
        }
        if (optB.getOptionByCode(option.getCodeOption()) != null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Option " + option.getCodeOption() + " already exists")
                    .build();
        }
        optB.addOption(option);
        return Response.ok(option).build();
    }

    // GET /options  |  GET /options?domaine=Mathématiques -> 200
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptions(@QueryParam("domaine") String domaine) {
        List<Option> list;
        if (domaine == null || domaine.isEmpty()) {
            list = optB.getListeOptions();
        } else {
            list = optB.getOptionsByDomaine(domaine);
        }
        return Response.ok(list).build();
    }

    // GET /options/{code} -> 200 | 404
    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOption(@PathParam("code") int code) {
        Option o = optB.getOptionByCode(code);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(o).build();
    }

    // PUT /options/{code} -> 200 | 404
    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOption(@PathParam("code") int code, Option option) {
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        option.setCodeOption(code); // keep the path code and the body code consistent
        if (!optB.updateOption(code, option)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(option).build();
    }

    // DELETE /options/{code} -> 204 | 404
    @DELETE
    @Path("/{code}")
    public Response deleteOption(@PathParam("code") int code) {
        if (!optB.deleteOption(code)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}