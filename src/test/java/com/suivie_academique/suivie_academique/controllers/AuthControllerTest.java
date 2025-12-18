package com.suivie_academique.suivie_academique.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import com.suivie_academique.suivie_academique.security.JwtUtils;
import com.suivie_academique.suivie_academique.utils.RolePersonnel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private PersonnelRepository personnelRepository;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtils jwtUtils;

    private Personnel personnel;
    private Map<String, String> registerRequest;
    private Map<String, String> loginRequest;

    @BeforeEach
    void setUp() {
        personnel = new Personnel();
        personnel.setCodePersonnel("ENS001");
        personnel.setNomPersonnel("DUPONT Jean");
        personnel.setLoginPersonnel("jdupont");
        personnel.setPhonePersonnel("699123456");
        personnel.setSexePersonnel("M");
        personnel.setRolePersonnel(RolePersonnel.ENSEIGNANT);
        personnel.setPadPersonnel("$2a$10$encodedPassword");

        registerRequest = new HashMap<>();
        registerRequest.put("loginPersonnel", "jdupont");
        registerRequest.put("nomPersonnel", "DUPONT Jean");
        registerRequest.put("phonePersonnel", "699123456");
        registerRequest.put("sexePersonnel", "M");
        registerRequest.put("rolePersonnel", "ENSEIGNANT");
        registerRequest.put("motDePasse", "password123");

        loginRequest = new HashMap<>();
        loginRequest.put("loginPersonnel", "jdupont");
        loginRequest.put("motDePasse", "password123");
    }

    @Test
    @WithMockUser
    void register_Success() throws Exception {
        when(personnelRepository.existsByLoginPersonnel("jdupont")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(personnelRepository.save(any(Personnel.class))).thenReturn(personnel);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur créé"));

        verify(personnelRepository, times(1)).existsByLoginPersonnel("jdupont");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(personnelRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    @WithMockUser
    void register_LoginAlreadyExists() throws Exception {
        when(personnelRepository.existsByLoginPersonnel("jdupont")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Login already used"));

        verify(personnelRepository, times(1)).existsByLoginPersonnel("jdupont");
        verify(personnelRepository, never()).save(any(Personnel.class));
    }

    @Test
    @WithMockUser
    void login_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(personnelRepository.findByLoginPersonnel("jdupont"))
                .thenReturn(Optional.of(personnel));
        when(jwtUtils.generateToken("jdupont")).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.codePersonnel").value("ENS001"))
                .andExpect(jsonPath("$.user.nomPersonnel").value("DUPONT Jean"))
                .andExpect(jsonPath("$.user.loginPersonnel").value("jdupont"))
                .andExpect(jsonPath("$.user.phonePersonnel").value("699123456"))
                .andExpect(jsonPath("$.user.sexePersonnel").value("M"))
                .andExpect(jsonPath("$.user.rolePersonnel").value("ENSEIGNANT"));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(personnelRepository, times(1)).findByLoginPersonnel("jdupont");
        verify(jwtUtils, times(1)).generateToken("jdupont");
    }

    @Test
    @WithMockUser
    void login_BadCredentials() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login ou mot de passe incorrect"));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(personnelRepository, never()).findByLoginPersonnel(anyString());
        verify(jwtUtils, never()).generateToken(anyString());
    }

    @Test
    @WithMockUser
    void login_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(personnelRepository.findByLoginPersonnel("jdupont"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Utilisateur introuvable"));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(personnelRepository, times(1)).findByLoginPersonnel("jdupont");
        verify(jwtUtils, never()).generateToken(anyString());
    }

    @Test
    @WithMockUser
    void register_WithAllRoles() throws Exception {
        String[] roles = {"ENSEIGNANT", "RESPONSABLE_ACADEMIQUE", "RESPONSABLE_PERSONNEL"};

        for (String role : roles) {
            Map<String, String> request = new HashMap<>(registerRequest);
            request.put("rolePersonnel", role);
            request.put("loginPersonnel", "user_" + role.toLowerCase());

            when(personnelRepository.existsByLoginPersonnel(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
            when(personnelRepository.save(any(Personnel.class))).thenReturn(personnel);

            mockMvc.perform(post("/api/auth/register")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @WithMockUser
    void register_WithMissingFields() throws Exception {
        Map<String, String> incompleteRequest = new HashMap<>();
        incompleteRequest.put("loginPersonnel", "jdupont");
        // Autres champs manquants

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteRequest)))
                .andExpect(status().isBadRequest()); // Le contrôleur ne valide pas les champs
    }

    @Test
    @WithMockUser
    void login_WithEmptyCredentials() throws Exception {
        Map<String, String> emptyRequest = new HashMap<>();
        emptyRequest.put("loginPersonnel", "");
        emptyRequest.put("motDePasse", "");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isUnauthorized());
    }
}