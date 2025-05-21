package com.gina.simulator.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for sending both tokens and username in response.
 */
@AllArgsConstructor
@Getter
public class AuthenticationResponse {

    @JsonProperty("access_token") private String accessToken;
    @JsonProperty("refresh_token") private String refreshToken;
    @JsonProperty("username") private String username;
}
