#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.security;

import java.util.List;

/**
 * The authorization and authentication services.
 *
 * @author Lucio Benfante <lucio.benfante@gmail.com>
 * @version ${symbol_dollar}Revision: 307a1ab9c32c ${symbol_dollar}
 */
public interface AuthService {

    public boolean checkLogin(String username, String password);

    public User getUserData(String username);
    
    public List<User> getUsers(List<String>usernames);
    
}
