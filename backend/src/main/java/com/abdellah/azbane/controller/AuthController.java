package com.abdellah.azbane.controller;

import com.abdellah.azbane.dto.request.AuthenticationRequest;
import com.abdellah.azbane.dto.request.RegisterRequest;
import com.abdellah.azbane.dto.response.AuthenticationResponse;
import com.abdellah.azbane.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Demande d'inscription reçue pour: {}", request.getEmail());
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        log.info("Demande de connexion reçue pour: {}", request.getEmail());
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la connexion: {}", e.getMessage());
            throw e;
        }
    }

    // Alias pour /authenticate
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        log.info("Demande de login reçue pour: {}", request.getEmail());
        return authenticate(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Pour JWT, le logout est géré côté client en supprimant le token
        return ResponseEntity.ok("Déconnexion réussie");
    }

    // Endpoint de test simple
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint fonctionne!");
    }

    @PostMapping("/test-post")
    public ResponseEntity<String> testPost(@RequestBody String body) {
        log.info("Test POST reçu avec body: {}", body);
        return ResponseEntity.ok("Test POST reçu: " + body);
    }
}
