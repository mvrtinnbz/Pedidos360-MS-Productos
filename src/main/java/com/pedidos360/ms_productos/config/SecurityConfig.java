package com.pedidos360.ms_productos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para permitir POST, PUT y DELETE en Postman
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite todas las peticiones temporalmente para pruebas locales
            );
        return http.build();
    }
}