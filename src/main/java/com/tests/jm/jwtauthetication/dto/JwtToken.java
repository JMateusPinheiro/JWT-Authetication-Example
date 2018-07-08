package com.tests.jm.jwtauthetication.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JwtToken {
    private final Claims claims;

    private static class Key {
        private static final String ROLE = "role";
    }

    public JwtToken(Claims claims) {
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Date expirationDate() {
        return claims.getExpiration();
    }

    public String getRole() {
        return claims.get(Key.ROLE, String.class);
    }

    public static JwtToken ofAccount(AccountCredentials user) {
        final Claims claims = Jwts.claims();
        claims.put(JwtToken.Key.ROLE, user.getRole());
        return new JwtToken(claims);
    }

    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole()));
    }

}