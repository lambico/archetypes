#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A result containing a single string.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@XmlRootElement
public class StringResult {

    private String content;

    public StringResult() {
    }
    
    public StringResult(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
