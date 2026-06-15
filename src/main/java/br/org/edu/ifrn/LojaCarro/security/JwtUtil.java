package br.org.edu.ifrn.LojaCarro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 3600000;

    public String gerarToken(String username, String papel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", papel);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extrairUsername(String token) {
        return extrairClaims(token).getSubject();
    }

    public String extrairPapel(String token) {
        return extrairClaims(token).get("role", String.class);
    }

    public boolean isTokenExpirado(String token) {
        return extrairClaims(token).getExpiration().before(new Date());
    }
}