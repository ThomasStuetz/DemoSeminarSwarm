package at.htl.demoseminarswarm.rest;

import at.htl.demoseminarswarm.model.Gender;
import at.htl.demoseminarswarm.model.Person;
import at.htl.demoseminarswarm.model.Repository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.logging.Logger;

@Path("/person")
@Api(value = "/person")
public class PersonEndpoint {

    private final static Logger LOGGER = Logger.getLogger(PersonEndpoint.class.getSimpleName());

    @Inject
    Repository repository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "find all persons")
    public Response doGet() {
        LOGGER.info("GET: " + repository.getRepository().size() + " Elemente zurückgegeben");
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
                LOGGER.info("GET (index): " + person);
                return Response.ok(person).build();
            } else {
                LOGGER.severe("GET (index): noContent für " + person);
                return Response.noContent().build();
            }
        } else {
            int id = index;
            Person person = repository.findById(id);

            if (person == null) {
                LOGGER.severe("GET (id): noContent für " + person);
                return Response.noContent().build();
            }
            LOGGER.info("GET (id): " + person);
            return Response.ok(person).build();
        }
    }

    @POST
    @Consumes({MediaType.TEXT_PLAIN, "application/json"})
    public Response create(JsonObject jsonPerson, @Context UriInfo uriInfo) {
        if (jsonPerson == null || jsonPerson.isEmpty()) {
            LOGGER.severe("POST: fehlgeschlagen --> " + jsonPerson);
            return Response.status(400).header("reason", "id darf nicht NULL sein").build();  // bad request
        }

        int index = repository.merge(repository.createObjectFromJson(jsonPerson));
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + index).build();
        LOGGER.info("POST: " + jsonPerson);
        return Response.created(uri).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(JsonObject jsonPerson, @Context UriInfo uriInfo) {

        if (jsonPerson == null || jsonPerson.isEmpty()) {
            LOGGER.severe("PUT: " + jsonPerson + " --> id darf nicht NULL sein");
            return Response.status(400).header("reason", "id darf nicht NULL sein").build();  // bad request
        }
        if (!jsonPerson.containsKey("id")) {
            LOGGER.severe("PUT: " + jsonPerson + " --> id darf nicht leer sein");
            return Response.status(400).header("reason", "id darf nicht leer sein").build();  // bad request
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
                    person.setLastname(jsonPerson.getString("lastname"));
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

        LOGGER.info("PUT: " + jsonPerson);
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{index}")
    public Response delete(@PathParam("index") int index) {
        if (index > 0 && index < repository.getRepository().size() - 1) {
            repository.delete(index);
            LOGGER.info("DELETE: Index = " + index);
        }
        LOGGER.info("DELETE fehlgeschlagen, da 'index' out of range");
        return Response.noContent().build();
    }
}