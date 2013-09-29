#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.rest;

import ${package}.core.bean.StringResult;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.lambico.dao.generic.Page;
import ${package}.core.bean.PaginatedResult;
import ${package}.core.bo.PersonBo;
import ${package}.core.dao.PersonDao;
import ${package}.core.po.Person;
import ${package}.core.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Some sample REST services.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@Path("/sample")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class SampleService {

    private static final Logger log = LoggerFactory.getLogger(SampleService.class);
    @Resource
    private PersonDao personDao;
    @Resource
    private PersonBo personBo;

    /**
     * A sample for a simple GET service, using path parameters and the security
     * context.
     *
     * @param message The message.
     * @param securityContext The security context.
     * @return A string with a welcome to the user and the message, showing the
     * username of the caller.
     */
    @GET
    @Path("/hello/{message}")
    public StringResult hello(@PathParam("message") String message,
            @Context SecurityContext securityContext) {
        log.debug("Calling hello.");
        Principal userPrincipal = securityContext.getUserPrincipal();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) userPrincipal;
        User user = (User) usernamePasswordAuthenticationToken.getPrincipal();
        String name = user.getUsername();
        return new StringResult("Hello, " + name + ": " + message);
    }

    @GET
    @Path("/initialize")
    public void initializeDb() throws ParseException {
        personBo.initializeArchive();
    }
    
    @GET
    @Path("/persons")
    public PaginatedResult<Person> getPersons(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        log.debug("Calling getPersons.");
        PaginatedResult<Person> result = new PaginatedResult<Person>();
        try {
            Page<Person> persons = personBo.getPaginatedPersons(page, pageSize);
            result.setItems(persons.getList());
            result.setPage(persons.getPage());
            result.setRowCount(persons.getRowCount());
            result.setLastPage(persons.getLastPage());
            result.setNextPage(persons.isNextPage());
            result.setPreviousPage(persons.isPreviousPage());
        } catch (Exception ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @GET
    @Path("/persons/{id}")
    public Person getPerson(@PathParam("id") Long id) {
        log.debug("Calling getPerson with {}.", id);
        Person person = personDao.get(id);
        if (person == null) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Person Not Found"));
            throw new WebApplicationException(builder.build());
        } else {
            return person;
        }
    }

    /**
     * Delete a person.
     *
     * @param id The identifier of the person.
     * @return
     */
    @DELETE
    @Path("/persons/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        log.debug("Calling deletePerson with {}.", id);
        Person person = personDao.get(id);
        if (person == null) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Person Not Found"));
            throw new WebApplicationException(builder.build());
        } else {
            personDao.delete(person);
        }
        return Response.ok().build();
    }

    /**
     * Add a Person.
     *
     * @param person The person data.
     * @return
     */
    @POST
    @Path("/persons")
    @Consumes({"application/json", "application/xml"})
    public Response addPerson(Person person) {
        log.info("Calling addPerson for {} {}.", person.getFirstName(), person.getLastName());
        List<Person> persons = personDao.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        if (persons != null && persons.size() > 0) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Person already existant."));
            throw new WebApplicationException(builder.build());
        } else {
            personDao.store(person);
        }
        return Response.ok().build();
    }

    /**
     * Update a person.
     *
     * @param membershipInfo The member data.
     * @return
     */
    @PUT
    @Path("/persons")
    @Consumes({"application/json", "application/xml"})
    public Response updatePerson(Person person) {
        log.debug("Calling updatePerson for {}.", person.getId());
        Person dbPerson = personDao.get(person.getId());
        if (dbPerson == null) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Person Not Found"));
            throw new WebApplicationException(builder.build());
        } else {
            dbPerson.setFirstName(person.getFirstName());
            dbPerson.setLastName(person.getLastName());
            dbPerson.setBirthDate(person.getBirthDate());
            personDao.store(person);
        }
        return Response.ok().build();
    }
}
