/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.rest;

import javax.ws.rs.Path;
import org.lambico.ws.archetype.template.core.dao.PersonDao;
import org.lambico.ws.archetype.template.core.po.Person;

/**
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@Path("/persons")
public class PersonService extends AbstractCrudRestService<Person, Long, PersonDao>{
    
}
