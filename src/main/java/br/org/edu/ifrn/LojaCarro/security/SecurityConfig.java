package br.org.edu.ifrn.LojaCarro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/usuarios")
                        .hasRole("GERENTE")

                        .requestMatchers(
                                "/auth/login")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/carro/salvar")
                        .hasRole("GERENTE")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/carro/**")
                        .hasRole("GERENTE")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/carro/**")
                        .hasRole("GERENTE")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/carro/**")
                        .hasAnyRole(
                                "GERENTE",
                                "VENDEDOR")

                        .anyRequest()
                        .authenticated()

                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}