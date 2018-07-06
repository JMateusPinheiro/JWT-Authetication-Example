package com.tests.jm.jwtauthetication.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import static com.tests.jm.jwtauthetication.utils.Constants.ROLE_USER;

@Data
@Builder
@ToString
public class AccountCredentials {
	private String username;
	private String password;
	private String role;

	// Exist in this moment only 1 role
	public static AccountCredentials ofPrincipal(UserDetails userDetails) {
	    String role = userDetails.getAuthorities().size() > 0 ? userDetails.getAuthorities().toArray()[0].toString() : ROLE_USER;
        return AccountCredentials.builder()
            .username(userDetails.getUsername())
            .password(userDetails.getPassword())
            .role(role)
                .build();
    }
}