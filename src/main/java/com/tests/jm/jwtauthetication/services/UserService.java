package com.tests.jm.jwtauthetication.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService {

    // Access the database (or any data storage) and get roles/authorities from the user
    public Collection<GrantedAuthority> getAuthoritiesFrom(String username) {
        if (username.equalsIgnoreCase("user")) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (username.equalsIgnoreCase("admin")) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else return Collections.emptyList();
    }

}
