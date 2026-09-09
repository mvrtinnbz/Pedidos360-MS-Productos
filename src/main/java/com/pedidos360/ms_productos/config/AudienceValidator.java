package com.pedidos360.ms_productos.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private static final String EXPECTED_AUDIENCE =
            "api://9415422a-7394-44ca-a7fb-911e767844a8";

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        if (jwt.getAudience() != null &&
            jwt.getAudience().contains(EXPECTED_AUDIENCE)) {

            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token no está destinado a Pedidos360-API",
                null
        );

        return OAuth2TokenValidatorResult.failure(error);
    }
}