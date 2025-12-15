package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.services.SalleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalleController {

    private final SalleService salleService;

    /**
     * POST /api/salles - Créer une salle
     */
    @PostMapping()
    public ResponseEntity<SalleDto> creerSalle(@RequestBody SalleDto salleDto) {
        try {
            SalleDto created = salleService.creerSalle(salleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/salles - Obtenir toutes les salles
     */
    @GetMapping
    public ResponseEntity<List<SalleDto>> getAllSalles() {
        return ResponseEntity.ok(salleService.getAllSalles());
    }

    /**
     * GET /api/salles/{code} - Obtenir une salle par code
     */
    @GetMapping("/{code}")

    public ResponseEntity<SalleDto> getSalleByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(salleService.getSalleByCode(code));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/salles/{code} - Modifier une salle
     */
    @PutMapping("/{code}")
    public ResponseEntity<SalleDto> updateSalle(
            @PathVariable String code,
            @RequestBody SalleDto salleDto) {
        try {
            return ResponseEntity.ok(salleService.updateSalle(code, salleDto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/salles/{code} - Supprimer une salle
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteSalle(@PathVariable String code) {
        try {
            salleService.deleteSalle(code);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/salles/statut/{statut} - Rechercher par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SalleDto>> rechercherParStatut(@PathVariable String statut) {
        return ResponseEntity.ok(salleService.rechercherParStatut(statut));
    }

    /**
     * GET /api/salles/libres?contenance=50 - Salles libres avec contenance min
     */
    @GetMapping("/libres")
    public ResponseEntity<List<SalleDto>> rechercherSallesLibres(
            @RequestParam(defaultValue = "0") int contenance) {
        return ResponseEntity.ok(salleService.rechercherSallesLibres(contenance));
    }

    /**
     * GET /api/salles/count/{statut} - Compter par statut
     */
    @GetMapping("/count/{statut}")
    public ResponseEntity<Long> compterParStatut(@PathVariable String statut) {
        return ResponseEntity.ok(salleService.compterParStatut(statut));
    }
}