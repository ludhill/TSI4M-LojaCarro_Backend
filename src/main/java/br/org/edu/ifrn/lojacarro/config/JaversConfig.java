package br.org.edu.ifrn.lojacarro.config;

import org.javers.spring.auditable.AuthorProvider;
// import org.javers.spring.auditor; // <-- Import obrigatório
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class JaversConfig {

    @Bean
    public AuthorProvider authorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "SISTEMA";
            }
            return auth.getName();
        };
    }
}