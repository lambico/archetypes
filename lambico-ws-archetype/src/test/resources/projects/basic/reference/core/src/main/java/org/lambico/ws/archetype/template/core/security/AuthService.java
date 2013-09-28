/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package org.lambico.ws.archetype.template.core.security;

import java.util.List;

/**
 * The authorization and authentication services.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 */
public interface AuthService {

    public boolean checkLogin(String username, String password);

    public User getUserData(String username);
    
    public List<User> getUsers(List<String>usernames);
    
}
