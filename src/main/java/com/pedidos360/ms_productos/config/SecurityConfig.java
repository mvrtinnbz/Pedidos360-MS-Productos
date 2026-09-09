package com.pedidos360.ms_productos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ISSUER =
            "https://sts.windows.net/0844a9ad-f458-47d0-8036-0f2080309ddc/";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // API REST: no utiliza sesiones ni CSRF
            .csrf(csrf -> csrf.disable())

            // La API funciona de manera stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // Todas las peticiones requieren autenticación
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )

            // Validación del JWT
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> {})
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder decoder =
                (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(ISSUER);

        // Valida el issuer del token
        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(ISSUER);

        // Valida que el token pertenezca a Pedidos360-API
        OAuth2TokenValidator<Jwt> audienceValidator =
                new AudienceValidator();

        // Ejecuta ambas validaciones
        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(
                    issuerValidator,
                    audienceValidator
                );

        decoder.setJwtValidator(validator);

        return decoder;
    }
}