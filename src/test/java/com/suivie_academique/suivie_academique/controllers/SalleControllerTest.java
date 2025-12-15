package com.suivie_academique.suivie_academique.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.security.JwtUtils;
import com.suivie_academique.suivie_academique.security.PersonnelDetailsService;
import com.suivie_academique.suivie_academique.services.SalleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalleController.class)
@Import(com.suivie_academique.suivie_academique.security.SecurityConfig.class)
class SalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SalleService salleService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private PersonnelDetailsService personnelDetailsService;

    private static final String TOKEN = "fake-jwt-token";

    private SalleDto salleDto;

    @BeforeEach
    void setupSecurity() {
        salleDto = new SalleDto("A101", "Salle info", 30, "LIBRE");

        when(jwtUtils.validateToken(TOKEN)).thenReturn(true);
        when(jwtUtils.getUsernameFromToken(TOKEN)).thenReturn("RESPONSABLE_PERSONNEL");

        User user = new User(
                "admin",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_RESPONSABLE_PERSONNEL"))
        );

        when(personnelDetailsService.loadUserByUsername("RESPONSABLE_PERSONNEL"))
                .thenReturn(user);
        System.out.println("Utilisateur a bien ete simuler");
    }

    // ---------- POST /api/salles ----------
    @Test
    void creerSalle_OK() throws Exception {
        when(salleService.creerSalle(any())).thenReturn(salleDto);

        mockMvc.perform(post("/api/salles")
                        .header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salleDto)))
                .andExpect(status().isCreated());
    }

    // ---------- GET /api/salles ----------
    @Test
    void getAllSalles_OK() throws Exception {
        when(salleService.getAllSalles()).thenReturn(List.of(salleDto));

        mockMvc.perform(get("/api/salles")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codeSalle").value("A101"));
    }

    // ---------- GET /api/salles/{code} ----------
    @Test
    void getSalleByCode_OK() throws Exception {
        when(salleService.getSalleByCode("A101")).thenReturn(salleDto);

        mockMvc.perform(get("/api/salles/A101")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeSalle").value("A101"));
    }

    @Test
    void getSalleByCode_NOT_FOUND() throws Exception {
        when(salleService.getSalleByCode("A100"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/salles/A100")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /api/salles/{code} ----------
    @Test
    void updateSalle_OK() throws Exception {
        when(salleService.updateSalle(eq("A101"), any()))
                .thenReturn(salleDto);

        mockMvc.perform(put("/api/salles/A101")
                        .header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salleDto)))
                .andExpect(status().isOk());
    }

    // ---------- DELETE /api/salles/{code} ----------
    @Test
    void deleteSalle_OK() throws Exception {
        doNothing().when(salleService).deleteSalle("A101");

        mockMvc.perform(delete("/api/salles/A101")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNoContent());
    }

    // ---------- GET /api/salles/statut/{statut} ----------
    @Test
    void rechercherParStatut_OK() throws Exception {
        when(salleService.rechercherParStatut("LIBRE"))
                .thenReturn(List.of(salleDto));

        mockMvc.perform(get("/api/salles/statut/LIBRE")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/salles/libres ----------
    @Test
    void rechercherSallesLibres_OK() throws Exception {
        when(salleService.rechercherSallesLibres(20))
                .thenReturn(List.of(salleDto));

        mockMvc.perform(get("/api/salles/libres")
                        .param("contenance", "20")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());
    }

    // ---------- GET /api/salles/count/{statut} ----------
    @Test
    void compterParStatut_OK() throws Exception {
        when(salleService.compterParStatut("LIBRE"))
                .thenReturn(3L);

        mockMvc.perform(get("/api/salles/count/LIBRE")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    // ---------- SÉCURITÉ ----------
    @Test
    void unauthorized_without_token() throws Exception {
        mockMvc.perform(get("/api/salles"))
                .andExpect(status().isUnauthorized());
    }
}
