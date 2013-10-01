/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import org.lambico.ws.archetype.template.core.bean.PaginatedResult;
import org.lambico.ws.archetype.template.core.po.Person;
import org.lambico.ws.archetype.template.core.test.BaseRSTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.lambico.test.spring.hibernate.junit4.FixtureSet;
import org.lambico.ws.archetype.template.core.bean.ItemResult;

/**
 * Test the REST PersonService.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@FixtureSet(modelClasses = {Person.class})
public class PersonServiceTest extends BaseRSTest {

    public static final String ENDPOINT_ADDRESS = "local://RestPersonService";
    public static final String DEFAULT_USERNAME = "username";
    public static final String DEFAULT_PASSWORD = "password";
    @Resource
    private PersonService personService;

    @Override
    protected String getServerEndpointAddress() {
        return ENDPOINT_ADDRESS;
    }

    @Override
    protected List<Class<?>> getServerResourceClasses() {
        List<Class<?>> result = new LinkedList<Class<?>>();
        result.add(PersonService.class);
        return result;
    }

    @Override
    protected List<ResourceProvider> getServerResourceProviders() {
        List<ResourceProvider> result = new LinkedList<ResourceProvider>();
        result.add(new SingletonResourceProvider(personService, true));
        return result;
    }

    /**
     * Test of getItems method, of class PersonService.
     */
    @Test
    public void testGetItems() {
        PersonService client = createProxyClient(PersonService.class, ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getItems(null, null);
        assertThat(persons.getItems(), hasSize(7));
    }

    /**
     * Test of getItems method, of class PersonService.
     */
    @Test
    public void testJsonGetItems() throws IOException {
        WebClient client = createWebClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, MediaType.APPLICATION_JSON);
        client.path("/persons");
        String result = client.get().readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        PaginatedResult<Person> persons = mapper.readValue(result, new TypeReference<PaginatedResult<Person>>() {
        });
        assertThat(persons.getItems(), hasSize(7));
    }

    /**
     * Test of getItem method, of class PersonService.
     */
    @Test
    public void testGetItem() {
        PersonService client = createProxyClient(PersonService.class, ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getItems(null, null);
        Person person1 = persons.getItems().get(0);
        Person person2 = client.getItem(person1.getId().toString()).getItem();
        assertThat(person2, is(not(nullValue())));
        assertThat(person2.getId(), equalTo(person1.getId()));
    }

    /**
     * Test of getItem method, of class PersonService.
     */
    @Test
    public void testJsonGetItem() throws IOException {
        WebClient client = createWebClient(ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, MediaType.APPLICATION_JSON);
        client.path("/persons");
        String result = client.get().readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        PaginatedResult<Person> persons = mapper.readValue(result, new TypeReference<PaginatedResult<Person>>() {
        });
        Person person1 = persons.getItems().get(0);
        client.back(true).path("/persons/{id}", person1.getId());
        result = client.get().readEntity(String.class);
        ItemResult<Person> irPerson2 = mapper.readValue(result, new TypeReference<ItemResult<Person>>() {
        });
        Person person2 = irPerson2.getItem();
        assertThat(person2, is(not(nullValue())));
        assertThat(person2.getId(), equalTo(person1.getId()));
    }

    /**
     * Test of deleteItem method, of class PersonService.
     */
    @Test
    public void testDeleteItem() {
        PersonService client = createProxyClient(PersonService.class, ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getItems(null, null);
        Person person = persons.getItems().get(0);
        Response response = client.deleteItem(person.getId().toString());
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        try {
            Person deletedPerson = client.getItem(person.getId().toString()).getItem();
        } catch (BadRequestException e) {
            assertThat(e.getResponse().getStatus(), equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
        }
    }

    /**
     * Test of addItem method, of class PersonService.
     */
    @Test
    public void testAddItem() {
        PersonService client = createProxyClient(PersonService.class, ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        Person newPerson = new Person();
        newPerson.setFirstName("New");
        newPerson.setLastName("Person");
        client.addItem(newPerson);
        PaginatedResult<Person> persons = client.getItems(null, null);
        assertThat(persons.getItems(), hasSize(8));
    }

    /**
     * Test of updateItem method, of class PersonService.
     */
    @Test
    public void testUpdateItem() {
        PersonService client = createProxyClient(PersonService.class, ENDPOINT_ADDRESS, DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
        PaginatedResult<Person> persons = client.getItems(null, null);
        Person person = persons.getItems().get(0);
        person.setFirstName("Updated");
        client.updateItem(person);
        Person updatedPerson = client.getItem(person.getId().toString()).getItem();
        assertThat(updatedPerson.getFirstName(), equalTo("Updated"));
    }
}