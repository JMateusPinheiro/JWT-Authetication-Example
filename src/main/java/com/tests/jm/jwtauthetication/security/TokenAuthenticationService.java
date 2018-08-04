package com.tests.jm.jwtauthetication.security;

import com.tests.jm.jwtauthetication.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class TokenAuthenticationService {
	private static final Logger LOGGER = LoggerFactory.getLogger("TokenAuthenticationService");
	private final UserService userService;

    TokenAuthenticationService(UserService userService) {
        this.userService = userService;
    }

    // EXPIRATION_TIME = 10 dias
	static final long EXPIRATION_TIME = 860_000_000;
	static final String SECRET = "MySecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	static void addAuthentication(HttpServletResponse response, String username) {
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();

		LOGGER.info("JWT ==> " + JWT);

		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}
	
	Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			// faz parse do token
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();

			LOGGER.info("USER ==> " + user);

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(
				        user,
                        null,
                        userService.getAuthoritiesFrom(user)
                );
			}
		}
		return null;
	}
	
}