package com.collieReact.controller;

import com.collieReact.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.collieReact.service.AuthService;
import com.collieReact.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String BEARER_PREFIX = "Bearer ";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginUser) {
        try {
            String token = authService.loginUser(loginUser);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/email")
    public ResponseEntity<Map<String, Object>> getEmailFromToken(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.getEmail(token.replace("Bearer ", ""));
            Map<String, Object> response = new HashMap<>();
            response.put("email", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Invalid token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith(BEARER_PREFIX)) {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Invalid Authorization header"));
            }

            String cleanedToken = token.substring(BEARER_PREFIX.length());
            authService.invalidateToken(cleanedToken);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully logged out");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Logout failed due to server error"));
        }
    }

}


