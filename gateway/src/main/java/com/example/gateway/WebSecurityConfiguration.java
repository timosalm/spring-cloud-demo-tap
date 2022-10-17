package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
class WebSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(oauth2Login -> {})
                .oauth2Client(oauth2Client -> {})
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/services/**").authenticated()
                        .pathMatchers("/**").permitAll()
                );
        return http.build();
    }
}