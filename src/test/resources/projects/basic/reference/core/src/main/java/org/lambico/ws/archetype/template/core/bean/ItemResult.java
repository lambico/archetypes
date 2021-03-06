/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.lambico.ws.archetype.template.core.po.Person;

/**
 * A bean for returning a single item.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@XmlRootElement(name = "result")
@XmlSeeAlso(Person.class)
public class ItemResult<T> {

    private T item;

    public ItemResult() {
    }
    
    public ItemResult(T item) {
        this.item = item;
    }
    
    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
    
}
