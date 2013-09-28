/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Server.
 */
package org.lambico.ws.archetype.template.server.security;

import javax.annotation.Resource;
import org.lambico.ws.archetype.template.core.security.AuthService;
import org.lambico.ws.archetype.template.core.security.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class ClientAuthenticationManager implements AuthenticationManager {

    @Resource
    private AuthService authServiceClient;

    public AuthService getAuthServiceClient() {
        return authServiceClient;
    }

    public void setAuthServiceClient(AuthService authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        if (authServiceClient.checkLogin((String) a.getPrincipal(), (String) a.getCredentials())) {
            User user = authServiceClient.getUserData((String) a.getPrincipal());
            return new UsernamePasswordAuthenticationToken(user, a.getCredentials(),
                    user.getAuthorities());
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}
