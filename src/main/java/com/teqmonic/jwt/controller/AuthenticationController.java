package com.teqmonic.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teqmonic.jwt.model.LoginResponse;
import com.teqmonic.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	private final TokenService tokenService;
	
	
	/**
	 * Authenticate the user and generate token
	 * The authentication can be handled at filter level as well
	 * 
	 * @param registration
	 * @return
	 */
	@GetMapping("/token")
	public LoginResponse getToken(Authentication authentication) {
		String token = tokenService.generateToken(authentication);
		return new LoginResponse(authentication.getName(), token);
	}
}
