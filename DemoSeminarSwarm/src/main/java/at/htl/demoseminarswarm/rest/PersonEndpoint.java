package at.htl.demoseminarswarm.rest;

import at.htl.demoseminarswarm.model.Repository;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

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
    public Response findPersonByIndex(@PathParam("index") int index){
        JsonObject person = repository.getPersonAtIndex(index);
        if (person != null) {
            return Response.ok(person).build();
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Consumes({"text/plain", "application/json"})
    public Response doPost(java.lang.String entity) {

        return Response.created(
                UriBuilder.fromResource(PersonEndpoint.class).build()).build();
    }

	@PUT
	@Consumes({"text/plain", "application/json"})
	public Response doPut(java.lang.String entity) {
		return Response.created(
				UriBuilder.fromResource(PersonEndpoint.class).build()).build();
	}

	@DELETE
	@Path("/{id}")
	public Response doDelete(@PathParam("id") java.lang.Long id) {
		return Response.noContent().build();
	}
}