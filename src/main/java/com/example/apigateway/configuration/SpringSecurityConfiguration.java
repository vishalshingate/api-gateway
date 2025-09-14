package com.example.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Spring Security configuration for enabling HTTP Basic Authentication in the API Gateway (WebFlux stack).
 *
 * <p>
 * IMPORTANT: Spring Cloud Gateway is reactive, so we must use ServerHttpSecurity / SecurityWebFilterChain
 * instead of servlet-based HttpSecurity / SecurityFilterChain.
 * </p>
 *
 * <b>Basic Authentication flow:</b>
 * <ul>
 *   <li>Client sends Authorization: Basic base64(username:password)</li>
 *   <li>Credentials are validated against the configured reactive user store</li>
 *   <li>401 is returned if missing/invalid</li>
 * </ul>
 *
 * <b>Customization points:</b>
 * <ul>
 *   <li>Add more users in userDetailsService()</li>
 *   <li>Adjust which paths are public via authorizeExchange()</li>
 *   <li>Replace in-memory users with a custom ReactiveUserDetailsService bean for DB-backed auth</li>
 * </ul>
 */
@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // Configure authorization rules
        http.authorizeExchange(ex -> ex
                .pathMatchers("/actuator/health", "/actuator/info").permitAll() // public examples
                .anyExchange().authenticated()
        );

        // Enable HTTP Basic
        http.httpBasic(Customizer.withDefaults());

        // Disable form login (not needed for APIs) and CSRF for stateless API
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("adminpass"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }
}
