package com.teqmonic.jwt.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

	private final JwtEncoder encoder;

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		
		String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				// only access authorities required, since this is applicable only to api calls
				.filter(authority -> !authority.startsWith("ROLE")) 
				.collect(Collectors.joining(" "));
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				//.expiresAt(now.plus(1, ChronoUnit.HOURS))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
		
		var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
		return this.encoder.encode(encoderParameters).getTokenValue();
	}

}
