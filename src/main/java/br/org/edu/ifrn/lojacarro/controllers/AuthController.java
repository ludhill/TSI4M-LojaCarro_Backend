package br.org.edu.ifrn.lojacarro.controllers;

import br.org.edu.ifrn.lojacarro.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String MASTER_KEY = "root";

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String userParam = credentials.get("username");
        String passParam = credentials.get("password");

        if (MASTER_KEY.equals(userParam) && MASTER_KEY.equals(passParam)) {
            String token = jwtUtil.generateToken(userParam);
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais invalidas!"));
    }
}
