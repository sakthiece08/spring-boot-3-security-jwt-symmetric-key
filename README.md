# spring-boot-3-security-jwt-symmetric-key

Symmetric Key: The same key is used for signing the token and verifying the signature. Asymmetric Key Pair: One key to sign the token and a different key is used to verify the signature.

There are pros and cons to each approach but using a shared key can simplify the development process and is faster. A good use case for this is when you have a SPA with a single backend and you want it to be stateless/sessionless (e.g to avoid issues with scaling up / sticky sessions / sharing sessions across instances).


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
