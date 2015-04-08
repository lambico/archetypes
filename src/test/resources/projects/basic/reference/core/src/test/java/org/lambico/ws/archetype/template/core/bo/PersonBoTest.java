/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.bo;

import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.lambico.dao.generic.Page;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;
import org.lambico.test.spring.hibernate.junit4.FixtureSet;
import org.lambico.ws.archetype.template.core.dao.PersonDao;
import org.lambico.ws.archetype.template.core.po.Person;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * Tests of the PersonBo methods.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@FixtureSet(modelClasses = {Person.class})
public class PersonBoTest extends AbstractBaseTest {

    @Resource
    private PersonBo personBo;
    @Resource
    private PersonDao personDao;

    /**
     * Test of initializeArchive method, of class PersonBo.
     */
    @Test
    public void testPopulateArchive() throws Exception {
        personBo.initializeArchive();
        List<Person> persons = personDao.findAll();
        assertThat(persons, hasSize(3));
    }

    /**
     * Test of getPaginatedPersons method, of class PersonBo.
     */
    @Test
    public void testGetPaginatedPersons() {
        Page<Person> paginatedPersons = personBo.getPaginatedPersons(null, null);
        assertThat(paginatedPersons.getList(), hasSize(7));
    }
}