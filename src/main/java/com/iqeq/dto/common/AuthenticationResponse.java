package com.iqeq.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("token_type")
    String tokenType;

    String scope;

    @JsonProperty("expires_in")
    String expiresIn;

    @JsonProperty("ext_expires_in")
    String extExpiresIn;

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("id_token")
    String idToken;

    private UserDto user;

    @JsonProperty("claims_token")
    String claimsToken;

    @JsonProperty("refresh_token")
    String refreshToken;

}
