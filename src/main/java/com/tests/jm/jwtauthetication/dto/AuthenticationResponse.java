package com.tests.jm.jwtauthetication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expirationDate;

    private AccountCredentials account;
}
