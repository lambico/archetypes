#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.rest;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.Test;
import org.junit.Ignore;
import ${package}.core.bean.PaginatedResult;
import ${package}.core.bean.StringResult;
import ${package}.core.po.Person;
import ${package}.core.test.BaseRSTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * Test the REST SampleService.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class SampleServiceTest extends BaseRSTest {

    public static final String ENDPOINT_ADDRESS = "local://RestSampleService";
    public static final String DEFAULT_USERNAME = "username";
    public static final String DEFAULT_PASSWORD = "password";
    @Resource
    private SampleService sampleService;

    @Override
    protected String getServerEndpointAddress() {
        return ENDPOINT_ADDRESS;
    }

    @Override
    protected List<Class<?>> getServerResourceClasses() {
        List<Class<?>> result = new LinkedList<Class<?>>();
        result.add(SampleService.class);
        return result;
    }

    @Override
    protected List<ResourceProvider> getServerResourceProviders() {
        List<ResourceProvider> result = new LinkedList<ResourceProvider>();
        result.add(new SingletonResourceProvider(sampleService, true));
        return result;
    }
    
    /**
     * Test of hello method, of class SampleService.
     */
    @Test
    public void testHello() {
        WebClient client = createWebClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        String message = "prova";
        client.path("/sample/hello/" + message);
        StringResult result = client.get(StringResult.class);
        assertThat(result.getContent(), equalTo("Hello, " + DEFAULT_USERNAME + ": " + message));
    }

    /**
     * Test of getPersons method, of class SampleService.
     */
    @Test
    public void testGetPersons() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        assertThat(persons.getItems(), hasSize(7));
    }

    /**
     * Test of getPersons method, of class SampleService.
     */
    @Test
    @Ignore
    public void testJsonGetPersons() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, MediaType.APPLICATION_JSON);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        assertThat(persons.getItems(), hasSize(7));
    }

    /**
     * Test of getPerson method, of class SampleService.
     */
    @Test
    public void testGetPerson() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        Person person1 = persons.getItems().get(0);
        Person person2 = client.getPerson(person1.getId());
        assertThat(person2, is(not(nullValue())));
        assertThat(person2.getId(), equalTo(person1.getId()));
    }

    /**
     * Test of deletePerson method, of class SampleService.
     */
    @Test
    public void testDeletePerson() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        Person person = persons.getItems().get(0);
        Response response = client.deletePerson(person.getId());
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        try {
            Person deletedPerson = client.getPerson(person.getId());
        } catch (BadRequestException e) {
            assertThat(e.getResponse().getStatus(), equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
        }
    }

    /**
     * Test of addPerson method, of class SampleService.
     */
    @Test
    public void testAddPerson() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        Person newPerson = new Person();
        newPerson.setFirstName("New");
        newPerson.setLastName("Person");
        client.addPerson(newPerson);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        assertThat(persons.getItems(), hasSize(8));
    }

    /**
     * Test of updatePerson method, of class SampleService.
     */
    @Test
    public void testUpdatePerson() {
        SampleService client = createProxyClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getPersons(null, null);
        Person person = persons.getItems().get(0);
        person.setFirstName("Updated");
        client.updatePerson(person);
        Person updatedPerson = client.getPerson(person.getId());
        assertThat(updatedPerson.getFirstName(), equalTo("Updated"));
    }


}