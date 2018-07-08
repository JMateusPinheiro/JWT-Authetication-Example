package com.tests.jm.jwtauthetication.security;

import com.tests.jm.jwtauthetication.dto.JwtToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.tests.jm.jwtauthetication.utils.Constants.EXPIRATION_TIME;
import static com.tests.jm.jwtauthetication.utils.Constants.SECRET;

@Component
public class JwtTokenCodec {

    public JwtToken decodeToken(String token) {
        return new JwtToken(Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody());
    }

    private Date generateExpirationDate(Date issuedAt) {
        return new Date(issuedAt.getTime() + EXPIRATION_TIME);
    }

    public String encodeToken(JwtToken token) {
        return Jwts.builder()
                .setClaims(token.getClaims())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(new Date()))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Date getExpirationDate(String token) {
        return decodeToken(token).expirationDate();
    }
}
