package com.abdellah.azbane.service;

import com.abdellah.azbane.dto.request.AuthenticationRequest;
import com.abdellah.azbane.dto.request.RegisterRequest;
import com.abdellah.azbane.dto.response.AuthenticationResponse;
import com.abdellah.azbane.exception.UserAlreadyExistsException;
import com.abdellah.azbane.model.User;
import com.abdellah.azbane.repository.UserRepository;
import com.abdellah.azbane.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Tentative d'inscription pour l'email: {}", request.getEmail());
        
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }
        
        // Créer le nouvel utilisateur
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .department(request.getDepartment())
                .position(request.getPosition())
                .phoneNumber(request.getPhoneNumber())
                .enabled(true)
                .build();
                
        var savedUser = userRepository.save(user);
        log.info("Utilisateur créé avec succès: {}", savedUser.getEmail());
        
        // Générer les tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        return buildAuthenticationResponse(savedUser, jwtToken, refreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentative de connexion pour l'email: {}", request.getEmail());
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            log.error("Échec de l'authentification pour l'email: {}", request.getEmail());
            throw new RuntimeException("Identifiants invalides");
        }
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        log.info("Connexion réussie pour l'utilisateur: {}", user.getEmail());
        
        return buildAuthenticationResponse(user, jwtToken, refreshToken);
    }
    
    private AuthenticationResponse buildAuthenticationResponse(User user, String accessToken, String refreshToken) {
        var userInfo = AuthenticationResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .department(user.getDepartment())
                .position(user.getPosition())
                .build();
        
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400) // 24 heures en secondes
                .userInfo(userInfo)
                .build();
    }
}
