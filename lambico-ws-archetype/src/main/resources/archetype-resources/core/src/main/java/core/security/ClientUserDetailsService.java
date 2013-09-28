#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.security;

import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * An {@link UserDetailsService} that use the UserDao for retrieving user details using remote WS.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public class ClientUserDetailsService implements UserDetailsService {

    @Resource
    private AuthService authServiceClient;

    public AuthService getAuthServiceClient() {
        return authServiceClient;
    }

    public void setAuthServiceClient(AuthService authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    /**
     * {@inheritDoc}
     *
     * @param username {@inheritDoc}
     * @return {@inheritDoc}
     * @throws UsernameNotFoundException {@inheritDoc}
     * @throws DataAccessException {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        UserDetails result = authServiceClient.getUserData(username);
        if (result == null) {
            throw new UsernameNotFoundException("Can't find user: " + username);
        }
        return result;
    }
}
