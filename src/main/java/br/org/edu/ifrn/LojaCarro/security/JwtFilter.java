package br.org.edu.ifrn.LojaCarro.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (!jwtUtil.isTokenExpirado(token)) {
                    String username = jwtUtil.extrairUsername(token);
                    String papel = jwtUtil.extrairPapel(token);


                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + papel);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {

                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}