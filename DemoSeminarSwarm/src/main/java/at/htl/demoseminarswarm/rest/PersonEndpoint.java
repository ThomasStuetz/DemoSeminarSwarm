package at.htl.demoseminarswarm.rest;

import at.htl.demoseminarswarm.model.Gender;
import at.htl.demoseminarswarm.model.Person;
import at.htl.demoseminarswarm.model.Repository;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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

    /**
     * Wird kein QueryParameter criteria="id" mitgegeben, wird nach dem Index gesucht,
     * ansonsten nach der Id
     *
     * @param index
     * @param criterium
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{index}")
    public Response findPersonByIndexOrId(
            @PathParam("index") int index,
            @DefaultValue("index") @QueryParam("criterium") String criterium
    ) {
        if (criterium.equals("index")) {
            Person person = repository.getPersonAtIndex(index);
            if (person != null) {
                return Response.ok(person).build();
            } else {
                return Response.noContent().build();
            }
        } else {
            int id = index;
            Person person = repository.findById(id);

            if (person == null) {
                return Response.noContent().build();
            }
            return Response.ok(person).build();
        }
    }

    @POST
    @Consumes({MediaType.TEXT_PLAIN, "application/json"})
    public Response create(JsonObject jsonPerson, @Context UriInfo uriInfo) {
        if (jsonPerson == null || jsonPerson.isEmpty()) {
            return Response.status(400).header("reason", "id darf nicht NULL sein").build();  // bad request
        }

        int index = repository.merge(repository.createObjectFromJson(jsonPerson));
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + index).build();
        return Response.created(uri).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(JsonObject jsonPerson, @Context UriInfo uriInfo) {

        if (jsonPerson == null || jsonPerson.isEmpty()) {
            return Response.status(400).header("reason", "id darf nicht NULL sein").build();  // bad request
        }
        if (!jsonPerson.containsKey("id")) {
            return Response.status(400).header("reason", "Id darf nicht leer sein").build();  // bad request
        }
        Person person = repository.findById(jsonPerson.getInt("id"));
        for (String key : jsonPerson.keySet()) {
            switch (key) {
                case "gender":
                    person.setGender(Gender.valueOf(jsonPerson.getString("gender")));
                    break;
                case "firstname":
                    person.setFirstname(jsonPerson.getString("firstname"));
                    break;
                case "lastname":
                    person.setFirstname(jsonPerson.getString("lastname"));
                    break;
                case "email" :
                    person.setEmail(jsonPerson.getString("email"));
                    break;
                case "country" :
                    person.setCountry(jsonPerson.getString("country"));
                    break;
                case "age":
                    person.setAge(jsonPerson.getInt("age"));
                    break;
                case "registered" :
                    person.setRegistered(jsonPerson.getBoolean("registered"));
            }
        }
        int index = repository.getRepository().indexOf(person);
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