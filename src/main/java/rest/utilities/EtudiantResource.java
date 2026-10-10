package rest.utilities;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/etudiants")
public class EtudiantResource {

    private static final EtudiantBusiness etB = new EtudiantBusiness();
    private static final OptionBusiness optB = new OptionBusiness();

    // POST /etudiants -> 200 | 404 (option inexistante)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant e) {
        if (e == null || e.getOption() == null
                || optB.getOptionByCode(e.getOption().getCodeOption()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        etB.addEtudiant(e);
        return Response.ok(e).build();
    }

    // GET /etudiants -> 200
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiants() {
        return Response.ok(etB.getAllEtudiants()).build();
    }

    // GET /etudiants/option?codeOption=1 -> 200 XML | 404 (option inexistante)
    @GET
    @Path("/option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEtudiantsByOption(@QueryParam("codeOption") int codeOption) {
        Option option = optB.getOptionByCode(codeOption);
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Etudiant> list = etB.getEtudiantsByOption(option);
        return Response.ok(new EtudiantList(list)).build();
    }

    // GET /etudiants/{id} -> 200 | 404
    @GET
    @Path("/{identifiant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiant(@PathParam("identifiant") String identifiant) {
        Etudiant e = etB.getEtudiantByIdentifiant(identifiant);
        if (e == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(e).build();
    }

    // PUT /etudiants/{id} -> 200 | 404
    @PUT
    @Path("/{identifiant}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("identifiant") String identifiant, Etudiant e) {
        if (e == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        e.setIdentifiant(identifiant);
        if (!etB.updateEtudiant(identifiant, e)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(e).build();
    }

    // DELETE /etudiants/{id} -> 204 | 404
    @DELETE
    @Path("/{identifiant}")
    public Response deleteEtudiant(@PathParam("identifiant") String identifiant) {
        if (!etB.deleteEtudiant(identifiant)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}