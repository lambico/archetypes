/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.security;

import java.util.List;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import org.apache.cxf.interceptor.security.AuthenticationException;

import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.codec.Base64;

/**
 * A request handler for managing authentication in tests.
 * 
 * Based on {@link https://github.com/gmazza/jersey-samples-on-cxf/blob/master/https-clientserver-grizzly/src/main/java/com/sun/jersey/samples/https_grizzly/auth/SecurityCXFJAXRSFilter.java}
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@Provider
public class SecurityCXFJAXRSFilter implements RequestHandler {

    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders httpHeaders;
    private static final Logger log = LoggerFactory.getLogger(SecurityCXFJAXRSFilter.class);
    
    private Map<String, User> users = new HashMap<String, User>();

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    @Override
    public Response handleRequest(Message inputMessage, ClassResourceInfo resourceClass) {
        Authentication authentication = authenticate();
        inputMessage.put(SecurityContext.class, new Authorizer(authentication));
        return null;
    }

    private Authentication authenticate() {
        // Extract authentication credentials
        List<String> authenticationList = httpHeaders.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authenticationList.size() < 1) {
            throw new AuthenticationException("Authentication credentials are required");
        }
        String authentication = authenticationList.get(0);
        if (!authentication.startsWith("Basic ")) {
            return null;
            // additional checks should be done here
            // "Only HTTP Basic authentication is supported"
        }
        authentication = authentication.substring("Basic ".length());
        String[] values = new String(Base64.decode(authentication.getBytes())).split(":");
        if (values.length < 2) {
            throw new WebApplicationException(400);
            // "Invalid syntax for username and password"
        }
        String username = values[0];
        String password = values[1];
        if ((username == null) || (password == null)) {
            throw new WebApplicationException(400);
        }

        // Validate the extracted credentials
        User user = users.get(username);
        UsernamePasswordAuthenticationToken auth = null;
        if (user != null && username.equals("username") && password.equals("password")) {
            auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            log.info("User {} authenticated.", username);
        } else {
            log.info("User {} not authenticated.", username);
            throw new AuthenticationException("Invalid username or password");
        }
        return auth;
    }

    public class Authorizer implements SecurityContext {

        private Authentication authentication;

        public Authorizer(final Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Principal getUserPrincipal() {
            return authentication;
        }

        @Override
        public boolean isUserInRole(String role) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean result = false;
            for (GrantedAuthority authority : authorities) {
                if (role.equals(authority.getAuthority())) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        @Override
        public boolean isSecure() {
            return "https".equals(uriInfo.getRequestUri().getScheme());
        }

        @Override
        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

}