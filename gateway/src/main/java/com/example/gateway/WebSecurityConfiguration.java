package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;

@EnableWebFluxSecurity
@Configuration
class WebSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/services/**").authenticated()
                .pathMatchers("/**").permitAll()
                .and()
                .oauth2Login(Customizer.withDefaults())
                .logout(logoutSpec -> {
                    logoutSpec.logoutHandler(logoutHandler()).logoutHandler(new WebSessionServerLogoutHandler()).logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository));
                }).build();
    }

    private ServerLogoutHandler logoutHandler() {
        return new DelegatingServerLogoutHandler(new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler());
    }

    private OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }
}