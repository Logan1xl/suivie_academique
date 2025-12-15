package com.suivie_academique.suivie_academique.services;


import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.entities.Salle;
import com.suivie_academique.suivie_academique.mappers.SalleMapper;
import com.suivie_academique.suivie_academique.repositories.SalleRepository;
import com.suivie_academique.suivie_academique.utils.SalleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalleServiceTest {

    @Mock
    private SalleRepository salleRepository;

    @Mock
    private SalleMapper salleMapper;

    @InjectMocks
    private SalleService salleService;

    private Salle salle;
    private SalleDto salleDto;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setCodeSalle("A101");
        salle.setDescSalle("Salle informatique");
        salle.setContenance(30);
        salle.setStatutSalle(SalleStatus.LIBRE);

        salleDto = new SalleDto();
        salleDto.setCodeSalle("A101");
        salleDto.setDescription("Salle informatique");
        salleDto.setContenance(30);
        salleDto.setStatusSalle("LIBRE");
    }

    @Test
    void creerSalle_success() {
        when(salleMapper.toEntity(salleDto)).thenReturn(salle);
        when(salleRepository.save(salle)).thenReturn(salle);
        when(salleMapper.toDto(salle)).thenReturn(salleDto);

        SalleDto result = salleService.creerSalle(salleDto);

        assertNotNull(result);
        assertEquals("A101", result.getCodeSalle());
        verify(salleRepository, times(1)).save(salle);
    }

    @Test
    void creerSalle_donneesInvalides() {
        salleDto.setContenance(5);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> salleService.creerSalle(salleDto)
        );

        assertEquals("Donnée invalide", exception.getMessage());
        verify(salleRepository, never()).save(any());
    }

    @Test
    void getAllSalles_success() {
        when(salleRepository.findAll()).thenReturn(List.of(salle));
        when(salleMapper.toDtoList(List.of(salle))).thenReturn(List.of(salleDto));

        List<SalleDto> result = salleService.getAllSalles();

        assertEquals(1, result.size());
        verify(salleRepository).findAll();
    }

    @Test
    void getSalleByCode_success() {
        when(salleRepository.findById("A101")).thenReturn(Optional.of(salle));
        when(salleMapper.toDto(salle)).thenReturn(salleDto);

        SalleDto result = salleService.getSalleByCode("A101");

        assertEquals("A101", result.getCodeSalle());
    }

    @Test
    void getSalleByCode_notFound() {
        when(salleRepository.findById("A101")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> salleService.getSalleByCode("A101"));
    }

    @Test
    void updateSalle_success() {
        when(salleRepository.findById("A101")).thenReturn(Optional.of(salle));
        when(salleRepository.save(any())).thenReturn(salle);
        when(salleMapper.toDto(any())).thenReturn(salleDto);

        SalleDto result = salleService.updateSalle("A101", salleDto);

        assertNotNull(result);
        verify(salleRepository).save(salle);
    }

    @Test
    void deleteSalle_success() {
        when(salleRepository.existsById("A101")).thenReturn(true);

        salleService.deleteSalle("A101");

        verify(salleRepository).deleteById("A101");
    }

    @Test
    void deleteSalle_notFound() {
        when(salleRepository.existsById("A101")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> salleService.deleteSalle("A101"));
    }

    @Test
    void rechercherParStatut_success() {
        when(salleRepository.findByStatutSalle(SalleStatus.LIBRE))
                .thenReturn(List.of(salle));
        when(salleMapper.toDtoList(any()))
                .thenReturn(List.of(salleDto));

        List<SalleDto> result = salleService.rechercherParStatut("LIBRE");

        assertEquals(1, result.size());
    }

    @Test
    void compterParStatut_success() {
        when(salleRepository.countByStatut(SalleStatus.LIBRE)).thenReturn(2L);

        long count = salleService.compterParStatut("LIBRE");

        assertEquals(2L, count);
    }
}
