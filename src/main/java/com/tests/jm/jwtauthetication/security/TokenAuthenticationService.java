package com.tests.jm.jwtauthetication.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

class TokenAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger("TokenAuthenticationService");
	
	// EXPIRATION_TIME = 10 dias
	private static final long EXPIRATION_TIME = 860_000_000;
	private static final String SECRET = "MySecret";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";


	// Execute in login
	static void addAuthentication(HttpServletResponse response, String username) {
		String JWT = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();

		LOGGER.info(String.format("USERNAME >> %s", username));
		LOGGER.info("JWT >> " + JWT);

		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	// Execute in other requests
	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			// faz parse do token
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();

			LOGGER.info("USER: " + user);

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
	
}