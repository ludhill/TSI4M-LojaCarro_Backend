package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");


        if ("gerente".equals(username) && "123".equals(password)) {
            String token = jwtUtil.gerarToken(username, "GERENTE");
            return ResponseEntity.ok(Map.of("token", token, "role", "GERENTE"));
        } else if ("vendedor".equals(username) && "123".equals(password)) {
            String token = jwtUtil.gerarToken(username, "VENDEDOR");
            return ResponseEntity.ok(Map.of("token", token, "role", "VENDEDOR"));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos"));
    }
}