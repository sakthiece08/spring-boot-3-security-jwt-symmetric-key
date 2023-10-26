package com.teqmonic.jwt.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomSecurityConfiguration {
	
	@Value("${jwt.key}")
	private String jwtKey;
	
	@Bean
	InMemoryUserDetailsManager users() {
		// below password details can be passed from externally as well, so clear text credentials can be hided in the source code
		UserDetails readUser = User.builder().username("user").password("{noop}password").authorities("ROLE_USER", "VIEW").build();
		UserDetails writeUser = User.builder().username("sak").password("{noop}password").authorities("ROLE_USER", "WRITE").build();
		return new InMemoryUserDetailsManager(readUser, writeUser);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	return http
			 .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/token").hasRole("USER")) // or hasRole("USER")
		     .authorizeHttpRequests(auth -> auth.requestMatchers("/api/view").hasAuthority("SCOPE_VIEW"))
		     .authorizeHttpRequests(auth -> auth.requestMatchers("/api/write").hasAuthority("SCOPE_WRITE"))
		     .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		     .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()))
		     .httpBasic(Customizer.withDefaults())
		     .build();	
	}
	
	@Bean
	JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
	}

	@Bean
	JwtDecoder jwtDecoder() {
		byte[] bytes = jwtKey.getBytes();
		SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
		return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
	}
	
	// set below converter bean to override default claims name - scopes. 
	// Read JwtGrantedAuthoritiesConverter.class for more details
	
	/**@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		//jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // claims in the jwt token
		//jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtConverter;
	} **/
}
