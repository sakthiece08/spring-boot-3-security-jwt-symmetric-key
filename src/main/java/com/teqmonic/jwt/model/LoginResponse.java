package com.teqmonic.jwt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(@JsonProperty("user_name")String user, @JsonProperty("jwt_token")String token) {

}
