/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.security;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * A simple auth service that simulate the existence of a user with
 * username=username, password=password and a role ROLE_DEFAULT.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class FakeAuthService implements AuthService {

    @Override
    public boolean checkLogin(String username, String password) {
        return true;
    }

    @Override
    public User getUserData(String username) {
        User user = createFakeUser();
        return user;
    }

    @Override
    public List<User> getUsers(List<String> usernames) {
        LinkedList<User> result = new LinkedList<User>();
        result.add(createFakeUser());
        return result;
    }

    private User createFakeUser() {
        User user = new User("username", "ec7b8c3f180cc77ff2b8c4486545e766", null, true, true, true, true);
        HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        user.setAuthorities(authorities);
        return user;
    }
}
