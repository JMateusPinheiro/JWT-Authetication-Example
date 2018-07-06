package com.tests.jm.jwtauthetication.security;


import com.tests.jm.jwtauthetication.dto.AccountCredentials;
import com.tests.jm.jwtauthetication.dto.AuthenticationRequest;
import com.tests.jm.jwtauthetication.dto.AuthenticationResponse;
import com.tests.jm.jwtauthetication.dto.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenCodec jwtTokenCodec;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenCodec jwtTokenCodec) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenCodec = jwtTokenCodec;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        LOGGER.info("LOGIN >> " + authenticationRequest.toString());

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final AccountCredentials userDetails = AccountCredentials.ofPrincipal((UserDetails) authentication.getPrincipal());

        final String token = jwtTokenCodec.encodeToken(JwtToken.ofAccount(userDetails));

        return ResponseEntity.ok(new AuthenticationResponse(token, "REFRESH_TOKEN_NOT_IMPLEMENTED", userDetails));
    }
}
