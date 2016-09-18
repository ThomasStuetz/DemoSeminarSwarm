package at.htl.demoseminarswarm.rest;

import at.htl.demoseminarswarm.model.Repository;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import java.net.URI;

@Path("/person")
public class PersonEndpoint {

    @Inject
    Repository repository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        return Response.ok(repository.getRepository()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{index}")
    public Response findPersonByIndex(@PathParam("index") int index) {
        JsonObject person = repository.getPersonAtIndex(index);
        if (person != null) {
            return Response.ok(person).build();
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Consumes({MediaType.TEXT_PLAIN, "application/json"})
    public Response create(JsonObject person, @Context UriInfo uriInfo) {

        int index = repository.add(person);
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + index).build();
        return Response.created(uri).build();
    }



    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(JsonObject person, @Context UriInfo uriInfo) {
        if (person == null || person.isEmpty()) {
            return Response.status(400).build();
        }
        int index = repository.update(person);
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + index).build();

        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{index}")
    public Response delete(@PathParam("index") int index) {
        if (index > 0 && index < repository.getRepository().size() - 1) {
            repository.delete(index);
        }
        return Response.noContent().build();
    }
}