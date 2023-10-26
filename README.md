# spring-boot-3-security-jwt-symmetric-key



## JWT components
```
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
	


```