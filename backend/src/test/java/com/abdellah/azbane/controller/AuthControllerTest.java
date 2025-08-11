package com.abdellah.azbane.controller;

import com.abdellah.azbane.dto.request.AuthenticationRequest;
import com.abdellah.azbane.dto.request.RegisterRequest;
import com.abdellah.azbane.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
        // Utiliser un des utilisateurs créés par DataInitializationService
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("admin@azbane.com")
                .password("admin123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()))
                .andExpect(jsonPath("$.user_info.email", is("admin@azbane.com")));
    }

    @Test
    public void testRegister() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("newuser@test.com")
                .password("password123")
                .role(Role.USER)
                .department("Testing")
                .position("QA Engineer")
                .phoneNumber("0123456789")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()))
                .andExpect(jsonPath("$.user_info.email", is("newuser@test.com")));
    }
    
    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("admin@azbane.com")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("UNAUTHORIZED")))
                .andExpect(jsonPath("$.message", is("Identifiants invalides")));
    }

    @Test
    public void testLoginWithNonExistentUser() throws Exception {
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("nonexistent@test.com")
                .password("anypassword")
                .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("UNAUTHORIZED")))
                .andExpect(jsonPath("$.message", is("Identifiants invalides")));
    }

    @Test
    public void testAuthenticationEndpoint() throws Exception {
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("manager@azbane.com")
                .password("manager123")
                .build();

        mockMvc.perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()))
                .andExpect(jsonPath("$.user_info.email", is("manager@azbane.com")))
                .andExpect(jsonPath("$.user_info.role", is("MANAGER")))
                .andExpect(jsonPath("$.token_type", is("Bearer")));
    }

    @Test
    public void testRegisterWithExistingEmail() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("admin@azbane.com") // Email qui existe déjà
                .password("password123")
                .role(Role.USER)
                .department("Testing")
                .position("QA Engineer")
                .phoneNumber("0123456789")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("CONFLICT")))
                .andExpect(jsonPath("$.message", is("Un utilisateur avec cet email existe déjà")));
    }

    @Test
    public void testAccessProtectedEndpointWithValidToken() throws Exception {
        // D'abord se connecter pour obtenir un token
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("engineer@azbane.com")
                .password("engineer123")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Extraire le token de la réponse
        String responseContent = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("access_token").asText();

        // Utiliser le token pour accéder à un endpoint protégé
        mockMvc.perform(get("/api/user/profile")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("engineer@azbane.com")))
                .andExpect(jsonPath("$.role", is("ENGINEER")));
    }

    @Test
    public void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isForbidden()); // 403 au lieu de 401
    }

    @Test
    public void testAccessProtectedEndpointWithInvalidToken() throws Exception {
        // Pas de token du tout -> rejet avec 403
        mockMvc.perform(get("/api/user/profile")
                .header("Authorization", "Bearer invalid-token-format"))
                .andExpect(status().isForbidden()); // Les erreurs JWT résultent en 403
    }

    @Test
    public void testAuthTestEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Test endpoint fonctionne!")));
    }
}
