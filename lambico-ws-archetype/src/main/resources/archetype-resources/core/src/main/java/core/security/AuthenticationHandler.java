#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.security;

import javax.ws.rs.core.Response;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for the authentication to services.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class AuthenticationHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationHandler.class);

    @Override
    public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
        log.debug("Checking authentication.");
        AuthorizationPolicy policy = (AuthorizationPolicy) m.get(AuthorizationPolicy.class);
        if (policy != null) {
            String username = policy.getUserName();
            log.debug("{} is authenticated.", username);
            return null;
        } else {
            // authentication failed, request the authetication, add the realm name if needed to the value of WWW-Authenticate 
            log.debug("Not yet authenticated.");
            return Response.status(401).header("WWW-Authenticate", "Basic").build();
        }
    }
}