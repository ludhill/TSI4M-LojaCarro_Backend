package br.org.edu.ifrn.lojacarro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Constantes para eliminar a duplicação de literals (java:S1192)
    private static final String ROLE_GERENTE = "GERENTE";
    private static final String ROLE_VENDEDOR = "VENDEDOR";
    private static final String ROTA_CARRO = "/carro/**";
    private static final String ROTA_CARROS = "/carros/**";

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @SuppressWarnings("java:S4502") // CSRF desabilitado é seguro pois a API é Stateless com JWT
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/auth/**", "/api/auth/**").permitAll()

                        .requestMatchers("/usuarios/**", "/usuario/**").hasRole(ROLE_GERENTE)

                        .requestMatchers(HttpMethod.GET, ROTA_CARRO, ROTA_CARROS)
                        .hasAnyRole(ROLE_GERENTE, ROLE_VENDEDOR, "CLIENTE")

                        .requestMatchers(HttpMethod.POST, ROTA_CARRO, ROTA_CARROS)
                        .hasAnyRole(ROLE_GERENTE, ROLE_VENDEDOR)

                        .requestMatchers(HttpMethod.PUT, ROTA_CARRO, ROTA_CARROS)
                        .hasAnyRole(ROLE_GERENTE, ROLE_VENDEDOR)

                        .requestMatchers(HttpMethod.DELETE, ROTA_CARRO, ROTA_CARROS)
                        .hasRole(ROLE_GERENTE)

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}