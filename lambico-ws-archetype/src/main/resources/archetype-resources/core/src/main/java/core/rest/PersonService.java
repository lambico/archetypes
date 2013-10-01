#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.rest;

import javax.ws.rs.Path;
import ${package}.core.dao.PersonDao;
import ${package}.core.po.Person;

/**
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@Path("/persons")
public class PersonService extends AbstractCrudRestService<Person, Long, PersonDao>{
    
}
