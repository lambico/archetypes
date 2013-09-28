#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import ${groupId}.dao.generic.Page;
import ${groupId}.dao.generic.PageDefaultImpl;
import ${groupId}.dao.spring.hibernate.HibernateGenericDao;
import ${package}.core.dao.PersonDao;
import ${package}.core.po.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A BO to be used for the tests of the generic DAO.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 * @version ${symbol_dollar}Revision: a8d8760b3552 ${symbol_dollar}
 */
@Service
public class PersonBo {

    private static final Logger log = LoggerFactory.getLogger(PersonBo.class);
    @Resource
    private PersonDao personDao;

    @Transactional
    public void initializeArchive() throws ParseException {
        log.debug("Executing initializeArchive.");
        personDao.deleteAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        createPerson("Mario", "Rossi", sdf.parse("25/04/1970"));
        createPerson("Francesca", "Verdi", sdf.parse("30/08/1990"));
        createPerson("Giovanni", "Bianchi", sdf.parse("15/03/1980"));
        log.debug("Created {} persons.", 3);
    }

    @Transactional(readOnly = true)
    public Page<Person> getPaginatedPersons(Integer page, Integer pageSize) {
        log.debug("Executing getPaginatedPersons.");
        DetachedCriteria crit = DetachedCriteria.forClass(Person.class);
        crit.addOrder(org.hibernate.criterion.Order.asc("lastName"))
                .addOrder(org.hibernate.criterion.Order.asc("firstName"));
        Page<Person> result;
        if (pageSize != null) {
            if (page == null) {
                page = new Integer(1);
            }
            result = ((HibernateGenericDao) personDao).searchPaginatedByCriteria(page, pageSize, crit);
        } else {
            List persons = ((HibernateGenericDao) personDao).searchByCriteria(crit);
            result = new PageDefaultImpl<Person>(persons, new Integer(1), persons.size(), persons.size());
        }
        log.debug("Retrieved {} persons.", result.getList().size());
        return result;
    }

    @Transactional(readOnly = true)
    public void printPerson(Long id) {
        Person p = personDao.read(id);
        if (p != null) {
            System.out.println(p.getFirstName() + " " + p.getLastName() + " "
                    + p.getBirthDate());
        } else {
            System.out.println("Person (" + id + ") not found");
        }
    }

    private void createPerson(String firstName, String lastName, Date birthDate) throws ParseException {
        List<Person> searches = null;
        searches = personDao.findByFirstNameAndLastName(firstName, lastName);
        if (searches.isEmpty()) {
            Person p = new Person();
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setBirthDate(birthDate);
            personDao.create(p);
        }
    }
}
