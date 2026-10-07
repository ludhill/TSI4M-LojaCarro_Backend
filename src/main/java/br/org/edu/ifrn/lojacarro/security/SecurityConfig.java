package br.org.edu.ifrn.lojacarro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROLE_GERENTE = "GERENTE";
    private static final String ROLE_VENDEDOR = "VENDEDOR";
    private static final String ROTA_CARRO = "/carro/**";
    private static final String ROTA_CARROS = "/carros/**";

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults()) // Ativa o suporte de CORS no Spring Security
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Libera todas as requisições de preflight (OPTIONS) do navegador
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}