package com.tests.jm.jwtauthetication.dto;

import lombok.Data;

@Data
public class AccountCredentials {
	private String username;
	private String password;
}