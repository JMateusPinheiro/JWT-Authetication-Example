package com.tests.jm.jwtauthetication.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountCredentials {
	private String username;
	private String password;
}