package com.tests.jm.jwtauthetication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
    private AccountCredentials account;
}
