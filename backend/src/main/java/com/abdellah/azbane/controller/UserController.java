package com.abdellah.azbane.controller;

import com.abdellah.azbane.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole().name());
        response.put("department", user.getDepartment());
        response.put("position", user.getPosition());
        response.put("phoneNumber", user.getPhoneNumber());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue " + user.getFullName() + " !");
        response.put("role", user.getRole().name());
        response.put("department", user.getDepartment());
        response.put("accessLevel", getAccessLevel(user));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnlyEndpoint() {
        return ResponseEntity.ok("Ceci est accessible uniquement aux administrateurs");
    }

    @GetMapping("/manager-plus")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<String> managerPlusEndpoint() {
        return ResponseEntity.ok("Ceci est accessible aux administrateurs et managers");
    }
    
    private String getAccessLevel(User user) {
        return switch (user.getRole()) {
            case ADMIN -> "Accès complet au système";
            case MANAGER -> "Gestion des projets et équipes";
            case ENGINEER -> "Accès aux données techniques";
            case SUPERVISOR -> "Supervision des opérations";
            case OPERATOR -> "Opérations sur le terrain";
            case USER -> "Consultation des données";
        };
    }
}
