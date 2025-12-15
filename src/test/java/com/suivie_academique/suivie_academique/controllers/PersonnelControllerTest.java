package com.suivie_academique.suivie_academique.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suivie_academique.suivie_academique.dto.PersonnelDto;
import com.suivie_academique.suivie_academique.services.PersonnelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonnelController.class)
class PersonnelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private PersonnelService personnelService;

    private PersonnelDto personnelDto;
    private Map<String, Object> requestMap;

    @BeforeEach
    void setUp() {
        personnelDto = new PersonnelDto();
        personnelDto.setCodePersonnel("ENS001");
        personnelDto.setNomPersonnel("DUPONT Jean");
        personnelDto.setLoginPersonnel("jdupont");
        personnelDto.setSexePersonnel("M");
        personnelDto.setPhonePersonnel("699123456");
        personnelDto.setRolePersonnel("ENSEIGNANT");

        requestMap = new HashMap<>();
        requestMap.put("nomPersonnel", "DUPONT Jean");
        requestMap.put("loginPersonnel", "jdupont");
        requestMap.put("sexePersonnel", "M");
        requestMap.put("phonePersonnel", "699123456");
        requestMap.put("rolePersonnel", "ENSEIGNANT");
        requestMap.put("motDePasse", "password123");
    }

    @Test
    @WithMockUser
    void creerPersonnel_Success() throws Exception {
        when(personnelService.creerPersonnel(any(PersonnelDto.class), eq("password123")))
                .thenReturn(personnelDto);

        mockMvc.perform(post("/api/personnel")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codePersonnel").value("ENS001"))
                .andExpect(jsonPath("$.nomPersonnel").value("DUPONT Jean"))
                .andExpect(jsonPath("$.loginPersonnel").value("jdupont"));

        verify(personnelService, times(1))
                .creerPersonnel(any(PersonnelDto.class), eq("password123"));
    }

    @Test
    @WithMockUser
    void creerPersonnel_BadRequest() throws Exception {
        when(personnelService.creerPersonnel(any(PersonnelDto.class), any()))
                .thenThrow(new RuntimeException("Login déjà utilisé"));

        mockMvc.perform(post("/api/personnel")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAll_Success() throws Exception {
        List<PersonnelDto> personnels = Arrays.asList(personnelDto);
        when(personnelService.getAll()).thenReturn(personnels);

        mockMvc.perform(get("/api/personnel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codePersonnel").value("ENS001"));

        verify(personnelService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    void getEnseignants_Success() throws Exception {
        List<PersonnelDto> enseignants = Arrays.asList(personnelDto);
        when(personnelService.getEnseignants()).thenReturn(enseignants);

        mockMvc.perform(get("/api/personnel/enseignants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rolePersonnel").value("ENSEIGNANT"));

        verify(personnelService, times(1)).getEnseignants();
    }

    @Test
    @WithMockUser
    void getByRole_Success() throws Exception {
        List<PersonnelDto> personnels = Arrays.asList(personnelDto);
        when(personnelService.getByRole("ENSEIGNANT")).thenReturn(personnels);

        mockMvc.perform(get("/api/personnel/role/ENSEIGNANT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rolePersonnel").value("ENSEIGNANT"));

        verify(personnelService, times(1)).getByRole("ENSEIGNANT");
    }

    @Test
    @WithMockUser
    void searchByNom_Success() throws Exception {
        List<PersonnelDto> personnels = Arrays.asList(personnelDto);
        when(personnelService.rechercherParNom("DUPONT")).thenReturn(personnels);

        mockMvc.perform(get("/api/personnel/search")
                        .param("nom", "DUPONT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nomPersonnel").value("DUPONT Jean"));

        verify(personnelService, times(1)).rechercherParNom("DUPONT");
    }

    @Test
    @WithMockUser
    void deletePersonnel_Success() throws Exception {
        doNothing().when(personnelService).deletePersonnel("ENS001");

        mockMvc.perform(delete("/api/personnel/ENS001")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(personnelService, times(1)).deletePersonnel("ENS001");
    }

    @Test
    @WithMockUser
    void deletePersonnel_NotFound() throws Exception {
        doThrow(new RuntimeException("Personnel introuvable"))
                .when(personnelService).deletePersonnel("ENS999");

        mockMvc.perform(delete("/api/personnel/ENS999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updatePersonnel_Success() throws Exception {
        when(personnelService.updatePersonnel(eq("ENS001"), any(PersonnelDto.class)))
                .thenReturn(personnelDto);

        mockMvc.perform(put("/api/personnel/ENS001")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personnelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codePersonnel").value("ENS001"));

        verify(personnelService, times(1))
                .updatePersonnel(eq("ENS001"), any(PersonnelDto.class));
    }

    @Test
    @WithMockUser
    void updatePersonnel_NotFound() throws Exception {
        when(personnelService.updatePersonnel(eq("ENS999"), any(PersonnelDto.class)))
                .thenThrow(new RuntimeException("Personnel introuvable"));

        mockMvc.perform(put("/api/personnel/ENS999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personnelDto)))
                .andExpect(status().isNotFound());
    }
}