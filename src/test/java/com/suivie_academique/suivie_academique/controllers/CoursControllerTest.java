package com.suivie_academique.suivie_academique.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suivie_academique.suivie_academique.dto.CoursDto;
import com.suivie_academique.suivie_academique.services.CourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoursController.class)
class CoursControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourService coursService;

    private CoursDto coursDto;

    @BeforeEach
    void setUp() {
        coursDto = new CoursDto();
        coursDto.setCodeCours("MATH101");
        coursDto.setLabelCours("Mathématiques I");
        coursDto.setDescCours("salle tres joviale");
        coursDto.setNbCreditCours("Credit 5");
        coursDto.setNbHeureCours("45H");
    }

    @Test
    @WithMockUser
    void creerCours_Success() throws Exception {
        when(coursService.creerCour(any(CoursDto.class))).thenReturn(coursDto);

        mockMvc.perform(post("/api/cours")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coursDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codeCours").value("MATH101"))
                .andExpect(jsonPath("$.labelCours").value("Mathématiques I"))
                .andExpect(jsonPath("$.descCours").value("salle tres joviale"))
                .andExpect(jsonPath("$.nbCreditCours").value("Credit 5"))
                .andExpect(jsonPath("$.nbHeureCours").value("45H"));

        verify(coursService, times(1)).creerCour(any(CoursDto.class));
    }

    @Test
    @WithMockUser
    void creerCours_BadRequest() throws Exception {
        when(coursService.creerCour(any(CoursDto.class)))
                .thenThrow(new RuntimeException("Code cours déjà existant"));

        mockMvc.perform(post("/api/cours")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coursDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAllCours_Success() throws Exception {
        List<CoursDto> coursList = Arrays.asList(coursDto);
        when(coursService.getAllCours()).thenReturn(coursList);

        mockMvc.perform(get("/api/cours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codeCours").value("MATH101"));

        verify(coursService, times(1)).getAllCours();
    }

    @Test
    @WithMockUser
    void deleteCours_Success() throws Exception {
        doNothing().when(coursService).deleteCours("MATH101");

        mockMvc.perform(delete("/api/cours/MATH101")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(coursService, times(1)).deleteCours("MATH101");
    }

    @Test
    @WithMockUser
    void deleteCours_NotFound() throws Exception {
        doThrow(new RuntimeException("Cours introuvable"))
                .when(coursService).deleteCours("MATH999");

        mockMvc.perform(delete("/api/cours/MATH999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}