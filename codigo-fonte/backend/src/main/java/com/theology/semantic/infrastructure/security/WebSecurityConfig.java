package com.theology.semantic.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Stateless APIs via JWT não precisam de CSRF Protection
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/actuator/**").permitAll() // Rotas de login e health liberadas
                .anyRequest().authenticated() // As rotas de manipulação de PDF exigem login
            );

        // Aqui injetaríamos o Filtro JWT customizado (ex: .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class))
        
        return http.build();
    }
}
