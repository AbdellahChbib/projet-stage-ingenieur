package com.abdellah.azbane.repository;

import com.abdellah.azbane.model.Role;
import com.abdellah.azbane.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // ========== Méthodes générées automatiquement par Spring Data JPA ==========
    // save(), findById(), findAll(), delete(), deleteById(), count(), etc.
    
    // ========== Requêtes dérivées (générées automatiquement) ==========
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Recherche par département (requête dérivée)
    List<User> findByDepartment(String department);
    
    // Recherche par rôle (requête dérivée)
    List<User> findByRole(Role role);
    
    // Recherche par nom et prénom (requête dérivée)
    List<User> findByFirstNameAndLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Recherche des utilisateurs actifs (requête dérivée)
    List<User> findByEnabledTrue();
    
    // Recherche des utilisateurs par rôle et statut (requête dérivée)
    List<User> findByRoleAndEnabled(Role role, Boolean enabled);
    
    // ========== Requêtes personnalisées avec @Query ==========
    @Query("SELECT u FROM User u WHERE u.department = :department AND u.enabled = true")
    List<User> findActiveUsersByDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);
    
    // Requête native SQL pour des cas complexes
    @Query(value = "SELECT * FROM users WHERE created_at >= :startDate ORDER BY created_at DESC", nativeQuery = true)
    List<User> findRecentUsers(@Param("startDate") String startDate);
}
