package com.tests.jm.jwtauthetication.security;


import com.tests.jm.jwtauthetication.dto.*;
import com.tests.jm.jwtauthetication.dto.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.logging.Logger;

import static com.tests.jm.jwtauthetication.utils.Constants.TOKEN_PREFIX;

@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenCodec jwtTokenCodec;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenCodec jwtTokenCodec) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenCodec = jwtTokenCodec;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                ));


        final AccountCredentials accountCredentials = AccountCredentials.ofPrincipal((UserDetails) authentication.getPrincipal());

        final JwtToken jwtToken = JwtToken.ofAccount(accountCredentials);

        final String token = jwtTokenCodec.encodeToken(jwtToken);

        final Date expirationDate = jwtToken.expirationDate();

        return ResponseEntity.ok(new AuthenticationResponse(token, expirationDate, accountCredentials));
    }

    @GetMapping("/auth/token")
    public ResponseEntity token(@RequestHeader("Authorization") String token) {
        if (!token.contains(TOKEN_PREFIX))
            return ResponseEntity.badRequest().body(Error.builder().field(token).message("Token invalid"));

        String tokenStr = token.substring(TOKEN_PREFIX.length());
        JwtToken jwtToken = jwtTokenCodec.decodeToken(tokenStr);

        if (jwtToken.isExpired())
            return ResponseEntity.badRequest().body(Error.builder().field(token).message("Token has expired"));

        final Date expirationDate = jwtToken.expirationDate();

        return ResponseEntity.ok(
                new AuthenticationResponse(
                        tokenStr,
                        expirationDate,
                        null
                )
        );
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity refreshToken(@RequestHeader("Authorization") String token) {
        if (!token.contains(TOKEN_PREFIX))
            return ResponseEntity.badRequest().body(Error.builder().field(token).message("Token invalid"));

        String tokenStr = token.substring(TOKEN_PREFIX.length());
        JwtToken jwtToken = jwtTokenCodec.decodeToken(tokenStr);

        final String refreshedToken = jwtTokenCodec.encodeToken(jwtToken);

        final Date expirationDate = jwtTokenCodec.getExpirationDate(refreshedToken);

        return ResponseEntity.ok(
                new AuthenticationResponse(
                        refreshedToken,
                        expirationDate,
                        null
                )
        );
    }
}
