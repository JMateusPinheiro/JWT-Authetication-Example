package com.tests.jm.jwtauthetication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tests.jm.jwtauthetication.dto.AccountCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		
		AccountCredentials credentials = new ObjectMapper()
				.readValue(request.getInputStream(), AccountCredentials.class);

		LOGGER.info("CREDENTIALS >> " + credentials.toString());

		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getUsername(), 
						credentials.getPassword(), 
						Collections.emptyList()
						)
				);
	}
	
	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain,
			Authentication auth) {

	    LOGGER.info("SUCCESS >> " + auth.getName() + " >> " + auth.getAuthorities());

		TokenAuthenticationService.addAuthentication(response, auth.getName());
	}

}