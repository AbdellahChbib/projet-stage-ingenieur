package com.abdellah.azbane.service;

import com.abdellah.azbane.model.Role;
import com.abdellah.azbane.model.User;
import com.abdellah.azbane.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeTestUsers();
    }

    private void initializeTestUsers() {
        if (userRepository.count() == 0) {
            log.info("Initialisation des utilisateurs de test...");
            
            // Créer un administrateur
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@azbane.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .department("IT")
                    .position("Administrateur Système")
                    .phoneNumber("0123456789")
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            
            // Créer un manager
            User manager = User.builder()
                    .firstName("Jean")
                    .lastName("Dupont")
                    .email("manager@azbane.com")
                    .password(passwordEncoder.encode("manager123"))
                    .role(Role.MANAGER)
                    .department("Infrastructure")
                    .position("Chef de Projet")
                    .phoneNumber("0123456788")
                    .enabled(true)
                    .build();
            userRepository.save(manager);
            
            // Créer un ingénieur
            User engineer = User.builder()
                    .firstName("Marie")
                    .lastName("Martin")
                    .email("engineer@azbane.com")
                    .password(passwordEncoder.encode("engineer123"))
                    .role(Role.ENGINEER)
                    .department("Études")
                    .position("Ingénieur Routier")
                    .phoneNumber("0123456787")
                    .enabled(true)
                    .build();
            userRepository.save(engineer);
            
            // Créer un utilisateur standard
            User user = User.builder()
                    .firstName("Pierre")
                    .lastName("Durand")
                    .email("user@azbane.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .department("Opérations")
                    .position("Technicien")
                    .phoneNumber("0123456786")
                    .enabled(true)
                    .build();
            userRepository.save(user);
            
            log.info("Utilisateurs de test créés avec succès:");
            log.info("Admin: admin@azbane.com / admin123");
            log.info("Manager: manager@azbane.com / manager123");
            log.info("Engineer: engineer@azbane.com / engineer123");
            log.info("User: user@azbane.com / user123");
        }
    }
}
