package com.abdellah.azbane.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("Tentative de création d'un utilisateur existant: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "CONFLICT");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailure(RuntimeException ex) {
        if (ex.getMessage().contains("Identifiants invalides")) {
            log.warn("Tentative de connexion avec identifiants invalides");
            
            Map<String, Object> response = new HashMap<>();
            response.put("error", "UNAUTHORIZED");
            response.put("message", "Identifiants invalides");
            response.put("timestamp", LocalDateTime.now());
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        // Pour les autres RuntimeException
        log.error("Erreur inattendue: {}", ex.getMessage(), ex);
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INTERNAL_SERVER_ERROR");
        response.put("message", "Une erreur interne s'est produite");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        response.put("error", "VALIDATION_FAILED");
        response.put("message", "Erreurs de validation");
        response.put("details", errors);
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
