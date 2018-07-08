package com.tests.jm.jwtauthetication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping({"", "/"})
	public ResponseEntity home() { return ResponseEntity.ok("Welcome!"); }

	@RequestMapping(value = "/users", produces = "application/json")
	public String getUsers() {
		return "{\"users\":[{\"name\":\"Lucas\", \"country\":\"Brazil\"}," +
		           "{\"name\":\"Jackie\",\"country\":\"China\"}]}";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/admins", produces = "application/json")
	public ResponseEntity getAdmins() {
        return ResponseEntity.ok(
                "[{'name': 'Adm', 'country': 'Brazil'}," +
                "{'name': 'Admin','country': 'China'}]");
    }

}