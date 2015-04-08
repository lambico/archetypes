#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.po;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.lambico.po.hibernate.EntityBase;

/**
 * An example of a persistent object containing personal data.
 *
 * @author <a href="mailto:lucio@benfante.com">Lucio Benfante</a>
 */
@javax.persistence.Entity
@XmlRootElement
@XmlSeeAlso(EntityBase.class)
public class Person extends EntityBase {

    private String firstName;
    private String lastName;
    private Date birthDate;

    public Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " (" + this.birthDate + ")";
    }
}
