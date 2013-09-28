/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.security;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.security.access.AccessDeniedException;

/**
 * Exception mapper for the {@link org.springframework.security.access.AccessDeniedException}.
 * 
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class SecurityExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

}