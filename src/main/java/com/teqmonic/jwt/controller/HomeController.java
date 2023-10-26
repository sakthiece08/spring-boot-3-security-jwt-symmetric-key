package com.teqmonic.jwt.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

	@GetMapping("/view")
	public String view(Principal principal) {
		return "<h1>Welcome to the secured read-only page, " +principal.getName() +" </h1>";
	}
	
	@GetMapping("/write")
	public String write(Principal principal) {
		return "<h1>Welcome to the secured write access page, " +principal.getName() +" </h1>";
	}

}
