package com.tests.jm.jwtauthetication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping(value = "/public/users", produces = "application/json")
	public ResponseEntity getUsers() {
		return ResponseEntity.ok("{ users: [{ 'name': 'Lucas' }, {'name': 'Jackie' }]}");
	}

	@GetMapping(value = "/countries", produces = "application/json")
	public ResponseEntity getCountries() {
		return ResponseEntity.ok("{ countries: [{ 'name': 'Brazil' }, { 'name': 'China' }]}");
	}

}